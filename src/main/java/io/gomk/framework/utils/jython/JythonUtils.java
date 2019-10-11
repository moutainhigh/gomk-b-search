package io.gomk.framework.utils.jython;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/***@ClassName:  JythonUtils   
* @Description:TODO(jython 工具类)   
* @author: zy
* @date:   2018年2月10日 下午5:24:40   
*     
* @Copyright: 2018 Inc. All rights reserved. 
* 注意：
*/

import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.poi.ss.formula.functions.T;
import org.python.core.Py;
import org.python.core.PyFunction;
import org.python.core.PyObject;
import org.python.core.PyString;
import org.python.core.PySystemState;
import org.python.util.PythonInterpreter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JythonUtils {
	

	/**
	 * @Title: jythonInit @Description: TODO(初始化jython) @param: @return @return:
	 * PythonInterpreter @throws
	 */
	public static PythonInterpreter jythonInit() {
		// 初始化site 配置
		Properties props = new Properties();
		props.put("python.home", "/System/Library/Frameworks/Python.framework/Versions/2.7"); // python Lib 或 jython Lib,根据系统中该文件目录路径
		props.put("python.console.encoding", "UTF-8");
		props.put("python.security.respectJavaAccessibility", "false");
		props.put("python.import.site", "false");
		Properties preprops = System.getProperties();
		PythonInterpreter.initialize(preprops, props, new String[0]);
		// 创建PythonInterpreter 对象
		PythonInterpreter interp = new PythonInterpreter();
		PySystemState sys = Py.getSystemState();
		sys.path.add("/System/Library/Frameworks/Python.framework/Versions/2.7/lib");
		interp.exec("import difflib");
		return interp;
	}

	/**
	 * @Title: loadPythonFile @Description: TODO(加载python 源码文件，) @param: @param
	 * interp @param: @param filePath
	 * ，比如：F:\\jpython_jar\\jpythonTest\\pythonTest.py
	 * 或/testpython/test.py @param: @return @return: PythonInterpreter @throws
	 */
	public static PythonInterpreter loadPythonFile(PythonInterpreter interp, String filePath) {
		interp.execfile(filePath);
		return interp;
	}

	/**
	 * @Title: loadPythonFunc @Description: TODO(加载python 源码文件中的某个方法) @param: @param
	 * interp @param: @param functionName @param: @return @return:
	 * PyFunction @throws
	 */
	public static PyFunction loadPythonFunc(PythonInterpreter interp, String functionName) {

		// 加载方法
		PyFunction func = (PyFunction) interp.get(functionName, PyFunction.class);
		return func;
	}

	/**
	 * @Title: execFunc @Description: TODO(执行无参方法,返回PyObject) @param: @param
	 * func @return: PyObject @throws
	 */
	public static PyObject execFunc(PyFunction func) {
		PyObject pyobj = func.__call__();
		return pyobj;
	}

	/**
	 * @Title: execFuncToString @Description: TODO(执行无参方法,返回一个字符串) @param: @param
	 * func @param: @return @return: String @throws
	 */
	public static String execFuncToString(PyFunction func) {
		PyObject pyobj = execFunc(func);
		return (String) pyobj.__tojava__(String.class);
	}

	/**
	 * @Title: execFuncToString @Description: TODO(执行有参方法,返回一个字符串) @param: @param
	 * func @param: @param paramName ，参数名 @param: @return @return: String @throws
	 */
	public static String execFuncToString2(PyFunction func, String paramName) {
		PyObject pyobj = func.__call__(new PyString(paramName));
		return (String) pyobj.__tojava__(String.class);
	}
	
	/**
	 * @Title: execFuncToString @Description: TODO(执行有参方法,返回一个字符串) @param: @param
	 * func @param: @param paramName ，参数名 @param: @return @return: String @throws
	 */
	public static String execFuncToString2(PyFunction func, String param1, String param2) {
		PyObject pyobj = func.__call__(new PyString(param1), new PyString(param2));
		return (String) pyobj.__tojava__(String.class);
	}

	/**
	 * @Title: execFuncToInteger @Description:
	 * TODO(执行无参方法,返回一个Integer) @param: @param func @param: @return @return:
	 * Integer @throws
	 */
	public Integer execFuncToInteger(PyFunction func) {
		PyObject pyobj = execFunc(func);
		return (Integer) pyobj.__tojava__(Integer.class);
	}

	/**
	 * @Title: execFuncToList @Description: TODO(执行无参方法,返回一个List) @param: @param
	 * func @param: @return @return: List<T> @throws
	 */
	public List<T> execFuncToList(PyFunction func) {
		PyObject pyobj = execFunc(func);
		return (List<T>) pyobj.__tojava__(List.class);
	}

	/**
	 * @Title: execFuncToMap @Description: TODO(执行无参方法,返回一个Map<String,
	 * Object>) @param: @param func @param: @return @return:
	 * Map<String,Object> @throws
	 */
	public Map<String, Object> execFuncToMap(PyFunction func) {
		PyObject pyobj = execFunc(func);
		return (Map<String, Object>) pyobj.__tojava__(Map.class);
	}

	public void execFuncToByParamsList(PyFunction func, List<T> paramsList) {

	}
	

	public static String getContrastResult(String str1, String str2) {
		
		StringBuilder sb = new StringBuilder();
		try {
			String filePath = "/Users/vko/Documents/git-code/gomk/testText.py";
			//String filePath = "/root/python/difflib/diffString.py";
		    String[] args1 = new String[] { "python", filePath, str1, str2};
		    Process proc = Runtime.getRuntime().exec(args1);// 执行py文件
		 
		    BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
		    String line = null;
		    
		    while ((line = in.readLine()) != null) {
		    	sb.append(line);
		       
		    }
		    in.close();
		    proc.waitFor();
		} catch (IOException e) {
		    e.printStackTrace();
		} catch (InterruptedException e) {
		    e.printStackTrace();
		}
		 System.out.println(sb.toString());
		return sb.toString();
	}

	public static void main(String[] args) {
//		PythonInterpreter interp = jythonInit();
//		// 文件名
//		String filePath = "/Users/vko/Documents/git-code/gomk/test.py";
//		interp = loadPythonFile(interp, filePath);
//		// 函数名
//		String functionName = "diffdo";
//		PyFunction func = loadPythonFunc(interp, functionName);
//		// 执行无参方法，返回PyObject
//		//PyObject pyobj = execFunc(func);
//		// 执行无参方法，返回String
//		//String resultStr = execFuncToString(func);
//		// 执行有参方法，返回String
//		String paramName = "name";
//		String resultStr2 = execFuncToString2(func, paramName, "222");
//		System.out.println("result:" + resultStr2);
//		
		try {
			String filePath = "/Users/vko/Documents/git-code/gomk/test.py";
		    String[] args1 = new String[] { "python", filePath, 
		    		"/Users/vko/Documents/git-code/gomk/v11.txt", 
		    		"/Users/vko/Documents/git-code/gomk/v12.txt" };
		    Process proc = Runtime.getRuntime().exec(args1);// 执行py文件
		 
		    BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
		    String line = null;
		    while ((line = in.readLine()) != null) {
		        System.out.println(line);
		    }
		    in.close();
		    proc.waitFor();
		} catch (IOException e) {
		    e.printStackTrace();
		} catch (InterruptedException e) {
		    e.printStackTrace();
		}
	}


}
