package booleanFormula;

/**
 * Class used to print the trace
 * Code taken from https://www.baeldung.com/java-print-binary-tree-diagram
 * @author Serge
 *
 */
public class TracePrinter {

	public TracePrinter() {
		// TODO Auto-generated constructor stub
	}
	

	
	public void print(StringBuilder buffer, String prefix, String childrenPrefix, Trace trace) {
        buffer.append(prefix);
        if(trace == null || trace.getRoot() == null) {
        	return;
        }
        buffer.append(trace.getRoot().toString());
        System.err.println(buffer.toString());
        buffer.append('\n');
        if(trace.getRight() != null) {
        	if(trace.getLeft()!= null) {
        		print(buffer, childrenPrefix + "├── ", childrenPrefix + "│   ",trace.getRight());
            	print(buffer, childrenPrefix + "└── ", childrenPrefix + "    ",trace.getLeft());
	        }else {
	        	print(buffer, childrenPrefix + "└── ", childrenPrefix + "    ",trace.getRight());
	        	
	        }
        }else {
        	if(trace.getLeft() != null) {
        		print(buffer, childrenPrefix + "└── ", childrenPrefix + "    ",trace.getLeft());
        	}
        }

    }
	
	public String traceString(Trace trace) {
		StringBuilder buffer = new StringBuilder(50);
        print(buffer, "", "",trace);
        return buffer.toString();
	}

}
