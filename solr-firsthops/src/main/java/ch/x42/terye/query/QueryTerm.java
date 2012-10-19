package ch.x42.terye.query;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.jcr.query.InvalidQueryException;

import ch.x42.terye.utils.DateUtils;

/**
 * This class represents one query term (e.g. 'msg:hello' that will be
 * translated into a Solr query statement.
 */
public class QueryTerm {

    // a regex defining the format of allowed query terms
    private static final Pattern pattern = Pattern
            .compile("([-])?([a-zA-Z]*):(.*)");

    private boolean negated;
    private String property;
    private String expression;

    public QueryTerm(String statement) throws InvalidQueryException {
        Matcher matcher = pattern.matcher(statement);
        if (!matcher.matches()) {
            // statement doesn't match the allowed format
            throw new InvalidQueryException("Invalid query: " + statement);
        }
        negated = matcher.group(1) == null ? false : true;
        property = matcher.group(2);
        expression = matcher.group(3);
    }

    /**
     * This method translates this term into and returns a Solr query string.
     * Query terms represent a condition on a property name and since properties
     * are stored as dynamic fields in Solr, the corresponding field name
     * consists of the property name and a type suffix (e.g. "msg_String" for
     * strings). Since we don't know the type of the property in advance (we
     * cannot deduce it from the query), we must create a disjunctive Solr query
     * listing all possible types like this:
     * 
     * msg_String:hello OR msg_Boolean:hello OR msg_Long:hello OR ...
     * 
     * Since some expressions cannot be converted to specific types (i.e. above
     * 'hello' can not be converted to a long), we must anticipate and not
     * include those types into the query (otherwise Solr throws an exception).
     * 
     * XXX: this is hacky... better solution?
     */
    public String toSolrQuery() {
        List<String> terms = new LinkedList<String>();

        // expressions are treated as strings by default
        terms.add(makeTerm("_String"));
        // all strings can be converted to booleans
        terms.add(makeTerm("_Boolean"));
        // check if expression can be parsed to a long
        try {
            Long.parseLong(expression);
            terms.add(makeTerm("_Long"));
        } catch (NumberFormatException e) {
        }
        // check if expression can be parsed to a double
        try {
            Double.parseDouble(expression);
            terms.add(makeTerm("_Double"));
        } catch (NumberFormatException e) {
        }
        // check if expression can be parsed to a date
        try {
            DateFormat formatter = new SimpleDateFormat(DateUtils.FORMAT);
            formatter.parse(expression.replaceAll("\"", ""));
            terms.add(makeTerm("_Date"));
        } catch (ParseException e) {
        }

        // assemble disjunctive query
        String query = "";
        Iterator<String> iterator = terms.iterator();
        while (iterator.hasNext()) {
            query += iterator.next();
            if (iterator.hasNext()) {
                query += " OR ";
            }
        }
        return query;
    }

    private String makeTerm(String suffix) {
        String field = property + suffix;
        if (negated) {
            // for a negated query we only want the docs that contain
            // the queried field but with a value other than 'expression'
            // (we don't want the docs that don't even contain 'field')
            return "(" + field + ":[* TO *] AND -" + field + ":" + expression
                    + ")";
        }
        return field + ":" + expression;
    }

}
