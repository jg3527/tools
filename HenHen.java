import java.awt.geom.Line2D;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class HenHen {
	private static HashSet<String> specialAuthorizationActions = new HashSet<String>(){{
		add("@With(TeamAuthorizationAction.class)");
		add("@With(InviteAuthorizationAction.class)");
		add("@With(QueueAuthorizationAction.class)");
		add("@With(EmploymentAuthorizationAction.class)");
	}};
	private static HashMap<String, String> routesMap = new HashMap<>();
	public static void main(String[] args){
//		List<String> ret = Arrays.asList("a", "b", "c");
//		System.out.println(ret.toString());
		File folder = new File("/Users/max/Downloads/henhen/");
		File result = new File("/Users/max/Downloads/henhen/result.csv");
		if(result.exists()){
			result.delete();
			result = new File("/Users/max/Downloads/henhen/result.csv");
		}
		try {
			HashSet<String> linesStartsWithAt = new HashSet<String>();
			BufferedWriter out =new BufferedWriter(new FileWriter(result));
			readRoutes();
			System.out.println(routesMap.toString());
			for(File file: folder.listFiles()){
				if(file.getName().startsWith("result"))
					continue;
				readFile(file, out, linesStartsWithAt);
			}
			
			debug("start words: " + linesStartsWithAt.toString());
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public static void debug(String str){
		//System.out.println("!!!! " + str);
	}
	
	public static void readRoutes(){
		File file = new File("/Users/max/Downloads/henhen/merchantbackend.routes");
		String line = "";
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			while((line = br.readLine()) != null){
				if(line.startsWith("#") || line.trim().equals(""))
					continue;
				debug(line);
				String functionName = line.substring(line.lastIndexOf(".") + 1, line.lastIndexOf("("));
				if(line.indexOf("@") == -1)
					continue;
				String route = line.substring(0, line.indexOf('@')).trim();
				if(routesMap.containsKey(functionName))
					debug("Warning!!!!!");
				routesMap.put(functionName, route);
			}
			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void readFile(File file, BufferedWriter out, HashSet<String> linesStartsWithAt){

		//TODO write down the file name
		//file = new File("/Users/max/Downloads/henhen/DealController.java");
		String authWords = "@With(MaxAuthenticationAction.class)";
		
		String roleAuthWords = "@MaxAuthorizationAnnotation";
		int actionNumber = 0;
		try {
			String fileName = file.getName();
			if(!fileName.endsWith("java")){
				return;
			}
			
			//out.write("\n");
//			debug(fileName);
//			debug(fileName.split(".").length + "");
			String controllerName = fileName.substring(0, fileName.indexOf('.'));
			debug("controller name: " + controllerName);
			//out.write("\n" + file.getName() + "\n" );
			//out.write("==============================\n");
			BufferedReader br = new BufferedReader(new FileReader(file));
				
			String line;
			String authAction = "authentication";
			String authorization = "authorization: ";
			Boolean allActionHasAuth = false;
			HashMap<String, Object> authChecks = new HashMap<String, Object>();
		
			int lineNumber = 0;
			String enter = "\n";
			String specialChecks = "";
			while((line = br.readLine() ) != null){
				lineNumber++;
				line = line.trim();
				
				if(line.startsWith("@")){
					if(line.startsWith(authWords)){
						authChecks.put(authAction, true);
					}else if(line.startsWith(roleAuthWords)){
						String roles =line.substring(line.indexOf("{") + 1,  line.indexOf("}"));
						roles = roles.replace(",", ";");
						authChecks.put(authorization, roles.trim());
					}else if(specialAuthorizationActions.contains(line)){
						specialChecks= line.substring(line.indexOf("(") + 1, line.indexOf("."));
						authChecks.put("specialAuth", specialChecks);
					}else if(line.startsWith("@With(value")) {
						String[] auths = line.substring(line.indexOf("{") + 1, line.indexOf("}")).split(",");
						
						if(auths[0].trim().startsWith("MaxAuthenticationAction.class"))
							authChecks.put(authAction, true);
						else{
							linesStartsWithAt.add(line);
						}
						if(auths.length > 1 ){
							if(auths[1].trim().startsWith("DealAuthorizationAction.class")){
								authChecks.put("specialAuth", "DealAuthorizationAction");
							}else{
								debug(auths[1]);
							}
							
						}
						
					}else if(line.equals("@With(value = {MaxAuthenticationAction.class})")){
						authChecks.put(authAction, true);
					}
					else{
						linesStartsWithAt.add(line);
					}
				}else if(!authChecks.isEmpty()){
					String write = "";
					if(line.startsWith("public Result")){
						String methodName = line.substring(line.indexOf('t') + 2, line.indexOf('(')).trim();
						String hasAuth = "no";
						if(allActionHasAuth || (authChecks.containsKey(authAction) && ((Boolean)authChecks.get(authAction)))){
							hasAuth = "yes";
						}
						String roles = "";
						if(authChecks.containsKey(authorization))
							roles = (String)authChecks.get(authorization);
						String specialAuth = "";
						if(authChecks.containsKey("specialAuth"))
							specialAuth = (String)authChecks.get("specialAuth");
						String route = "";
						if(routesMap.containsKey(methodName))
							route = routesMap.get(methodName);
						write = controllerName + "," + route + "," + methodName + "," + hasAuth + "," + roles + "," + specialAuth;
						actionNumber++;
						out.write(write + enter); 
					}else if(line.startsWith("public class")){
						allActionHasAuth = true;
						//write = "All the actions in this controller has " + authChecks.toString() + "checks";
					}else{
						System.out.println("file Name " + file.getName() + "line: " + lineNumber);
					}
					//debug("1 " + write);
					
					authChecks = new HashMap<String, Object>();
					
				}else if(line.startsWith("public Result")){
					String methodName = line.substring(line.indexOf('t') + 2, line.indexOf('(')).trim();
					String hasAuth = "no";
					if(allActionHasAuth)
						hasAuth = "yes";
					String route = "";
					if(routesMap.containsKey(methodName))
						route = routesMap.get(methodName);
					String write = controllerName + "," + route + "," + methodName + "," + hasAuth;
					//String write = methodName//methodName + ": " + authChecks.toString();
					debug("2" + write);
					out.write(write + enter);
					actionNumber++;
				}
				
			}
			//out.write("Number of actions: " + actionNumber + "\n");
			br.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
				
		}
		
	}
}
 