import java.util.*;
/*so in this part, Stack.get(0)is global vars in list
 * 
 * 
 */
public class Symbol_Table {
	private Stack<LinkedList<String>> symbolTable = new Stack<LinkedList<String>>();
	public int size() {return symbolTable.size();}
	//a new list of variable names is created to hold the variables in this new block
	public void enter(LinkedList<String> item) {
		//System.out.println("push item");
		symbolTable.push(item);
	}
	//When a block is exited, the list for that block is popped from
	//the stack.
	public void exit() {
		symbolTable.pop();
	}
	
	public boolean search(String name) {
		//the symbol table would be searched beginning with the newest block to locate the most recent
		//symbol table entry with the given name.
		for (int i= symbolTable.size()-1;i>=0;i--) {
			if(symbolTable.get(i).contains(name)) return true;
		}
		return false;
	}
	
	public int searchscope(String name) {
		for (int i= symbolTable.size()-1;i>=0;i--) {
			if(symbolTable.get(i).contains(name)) return i;
		}
		return -1;
	}
	//it is an error if the variable is not found within that level,
	//even if the variable is currently in scope
	public boolean checkthelvl(String name, int lvl) {
		int place = symbolTable.size()-1;
		if(lvl > place) return false; //cant find the value out of the table
		
		return (symbolTable.get(place-lvl).contains(name));//check if can get the var in specifies scope
	}
	
	// check if the name is redeclared, if not add it in the list
	public boolean checkorAdd(String name) {
		if(symbolTable.get(symbolTable.size()-1).contains(name)) {
			System.err.println("redeclaration of variable " + name);
			return false;
		}
		else {
			symbolTable.get(symbolTable.size()-1).add(name);
			return true;
		}
		
	}
	
	public boolean checkGloabl(String name) {
		return (symbolTable.get(0).contains(name));
	}

}

