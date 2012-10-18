package ch.x42.terye.query;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.jcr.query.InvalidQueryException;

public class QueryTerm {

    private static final Pattern pattern = Pattern
            .compile("([-])?([a-zA-Z]*):(.*)");

    private boolean negated;
    private String property;
    private String expression;

    public QueryTerm(String statement) throws InvalidQueryException {
        Matcher matcher = pattern.matcher(statement);
        if (!matcher.matches()) {
            throw new InvalidQueryException("Invalid query: " + statement);
        }
        negated = matcher.group(1) == null ? false : true;
        property = matcher.group(2);
        expression = matcher.group(3);
    }

    public String toSolrString() {
        List<String> clauses = new LinkedList<String>();

        clauses.add(makeClause("_String"));
        clauses.add(makeClause("_Boolean"));
        try {
            Long.parseLong(expression);
            clauses.add(makeClause("_Long"));
        } catch (NumberFormatException e) {
        }
        try {
            Double.parseDouble(expression);
            clauses.add(makeClause("_Double"));
        } catch (NumberFormatException e) {
        }

        String s = "";
        Iterator<String> i = clauses.iterator();
        while (i.hasNext()) {
            s += i.next();
            if (i.hasNext()) {
                s += " OR ";
            }
        }
        return s;
    }

    private String makeClause(String suffix) {
        String field = property + suffix;
        return "(" + field + ":[* TO *] AND " + (negated ? "-" : "") + field
                + ":" + expression + ")";
    }

}
