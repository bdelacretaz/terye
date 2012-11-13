package ch.x42.terye.persistence.hbase;

import java.io.DataInput;
import java.io.IOException;
import java.util.regex.Pattern;

import org.apache.hadoop.hbase.filter.WritableByteArrayComparable;
import org.apache.hadoop.hbase.util.Bytes;

public class RegexStringComparator extends WritableByteArrayComparable {

    private Pattern pattern;

    public RegexStringComparator() {
    }

    public RegexStringComparator(String expr) {
        super(Bytes.toBytes(expr));
        this.pattern = Pattern.compile(expr, Pattern.DOTALL);
    }

    @Override
    public int compareTo(byte[] value, int offset, int length) {
        String rowKey = new String(value, offset, length);
        return pattern.matcher(rowKey).matches() ? 0 : 1;
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        super.readFields(in);
        this.pattern = Pattern.compile(Bytes.toString(getValue()));
    }

}
