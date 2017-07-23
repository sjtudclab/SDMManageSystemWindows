package shell;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
public class RunShell {

	 public static void main(String[] args) {
	        try {
	            Process process = Runtime.getRuntime().exec("call_mkdir.sh");
	            InputStreamReader ir = new InputStreamReader(process.getInputStream());
	            LineNumberReader input = new LineNumberReader(ir);
	            String line;
	            while((line = input.readLine()) != null)
	                System.out.println(line);
	            input.close();
	            ir.close();
	        } catch (IOException e) {
	            // TODO: handle exception
	            e.printStackTrace();
	        }
	    }
}
