import java.util.LinkedList;

/* *** This file is given as part of the programming assignment. *** */

public class Parser {

	private Symbol_Table symbolTable  = new Symbol_Table(); // call symboltable class to have a symboltable to modify.
    // tok is global to all these parsing methods;
    // scan just calls the scanner's scan method and saves the result in tok.
    private Token tok;// the current token
    //private int blanknum = 0;
    private int scopeDepth = -1;
    private void scan() {
	tok = scanner.scan();
    }

    private Scan scanner;
    Parser(Scan scanner) {
	this.scanner = scanner;
	scan();
	program();
	if( tok.kind != TK.EOF )
	    parse_error("junk after logical end of program");
    }
    //handle the blank before every block
    private void printBlank(int num)
    {
    	for(int i=0;i<2*num;i++)
    	{
    		System.out.print(" ");
    	}
    }
    // two print to make print easier
    private void printc(String str)
    {
    	System.out.print(str);
    }
    private void printcl(String str)
    {
    	System.out.println(str);
    }
    private void program() {
    printcl("#include <stdio.h>");
    printc("int main()");
	block();
    }

    private void block(){
    //System.out.println(symbolTable.size());
    //printBlank(blanknum);
    printcl("{");
    //blanknum++;
    scopeDepth++;
    symbolTable.enter(new LinkedList<String>());//when block up, a new list enter.
	declaration_list();
	statement_list();
	symbolTable.exit();//when block done, the list done.
	scopeDepth--;
	//blanknum--;
	//printBlank(blanknum);
	printcl("}");
    }

    private void declaration_list() {
	// below checks whether tok is in first set of declaration.
	// here, that's easy since there's only one token kind in the set.
	// in other places, though, there might be more.
	// so, you might want to write a general function to handle that.
	while( is(TK.DECLARE) ) {
	    declaration();
	}
    }

    private void declaration() {
	mustbe(TK.DECLARE);
	//mustbe(TK.ID); // check ID and give the right situation
	//printBlank(blanknum);	
	if(is(TK.ID)) {
		if(symbolTable.checkorAdd(tok.string)) {
			printc("int " );
			printcl(tok.string +"_"+scopeDepth+";"); // var = tok.string
		}
		scan();
	}
	else mustbe(TK.ID); // can use mustbe to exit and print error, it cant be right when in the else case :).
	
	while( is(TK.COMMA) ) {
	    scan();
		if(is(TK.ID)) {
			if(symbolTable.checkorAdd(tok.string)) {
				printc("int " );
				printcl(tok.string +"_"+scopeDepth+";"); // var = tok.string
			}
			scan();
		}
		else mustbe(TK.ID);
	}
    }

	private void statement_list() {
    	while(isStatement_list()) statement();
    }
    

    private void statement() {
		// TODO Auto-generated method stub
    	if(isRef_id()) assignment();
    	else if(is(TK.PRINT)) PT_case();
    	else if(is(TK.DO)) DO_case();
    	else if(is(TK.IF)) IF_case();
	}

	private boolean isStatement_list() {
		// TODO Auto-generated method stub
		if(isRef_id()||is(TK.PRINT)||is(TK.DO)||is(TK.IF)) return true;
		return false;
	}

	private void IF_case() {
		// TODO Auto-generated method stub
		mustbe(TK.IF);
		//blanknum++;
		//printBlank(blanknum);
		printc("if(");
		guarded_command();
		while(is(TK.ELSEIF)) {
			//printBlank(blanknum);
			printc("else if(");
			scan();
			guarded_command();
		}
		
		if(is(TK.ELSE)) {
			//printBlank(blanknum);
			printcl("else");
			scan();
			block();
		}
		mustbe(TK.ENDIF);
		//blanknum--;
	}

	private void guarded_command() {
		// TODO Auto-generated method stub
		expr();
		printcl("<=0)");
		mustbe(TK.THEN);
		block();
	}

	private void expr() {
		// TODO Auto-generated method stub
		term();
		while(isAddop()) {
			printc(" "+tok.string+" ");
			scan();
			term();
		}
	}

	private boolean isAddop() {
		// TODO Auto-generated method stub
		if(is(TK.PLUS)||is(TK.MINUS)) return true;
		return false;
	}

	private void term() {
		// TODO Auto-generated method stub
		factor();
		while(isMultop()) {
			printc(" "+tok.string+" ");
			scan();
			factor();
		}
	}

	private void factor() {
		// TODO Auto-generated method stub
		if(is(TK.LPAREN)) {
			printc("(");
			//int tempB = blanknum;
			//blanknum = 0;
			scan();
			expr();
			mustbe(TK.RPAREN);
			//blanknum = tempB;
			printc(")");
		} 
		else if(isRef_id()) ref_id();
		else if(is(TK.NUM)) {
			printc(tok.string);
			scan();
		}
	}

	private void ref_id() {
		// TODO Auto-generated method stub
		if(is(TK.REF)) {
			int num = -1;
			scan();
			if(is(TK.NUM)) {
				num = Integer.parseInt(tok.string);
				scan();//get var after"~"
			}
			if(num == -1) {
				if(symbolTable.checkGloabl(tok.string)) {
					//printBlank(blanknum);
					printc(tok.string+"_0");
				}
				else {
					System.err.println("no such variable ~" + tok.string+" on line " + tok.lineNumber);
					System.exit(1);
				}
			}
			else{
				if(symbolTable.checkthelvl(tok.string,num)){
					//printBlank(blanknum);
					printc(tok.string+"_"+(scopeDepth-num));
				}
				else {
					System.err.println("no such variable ~" + num 
							+ tok.string+" on line " + tok.lineNumber);
					System.exit(1);
				}
				
			}		
		
		}
		else {
			if(symbolTable.search(tok.string)) {	
				//printBlank(blanknum);
				printc(tok.string+"_" +symbolTable.searchscope(tok.string));
			}
			else {
				System.err.println(tok.string + " is an undeclared variable on line " + tok.lineNumber);
				
				System.exit(1);
			}

		}
		mustbe(TK.ID);
	}

	private boolean isRef_id() {
		// TODO Auto-generated method stub
		if(is(TK.REF) || is(TK.ID)) return true;
		return false;
	}

	private boolean isMultop() {
		// TODO Auto-generated method stub
		if(is(TK.DIVIDE)||is(TK.TIMES)) return true;
		return false;
	}

	private void DO_case() {
		// TODO Auto-generated method stub
		mustbe(TK.DO);
		printc("while(");
		guarded_command();
		mustbe(TK.ENDDO);
	}

	private void PT_case() {
		// TODO Auto-generated method stub
		//printBlank(blanknum);
		printc("printf(\"%d\\n\", ");
		//int temB = blanknum;
		//blanknum = 0;
		mustbe(TK.PRINT);
		expr();
		//blanknum = temB;
		printcl(");");
	}

	private void assignment() {
		// TODO Auto-generated method stub
		ref_id();
		mustbe(TK.ASSIGN);
		printc(" = ");
		expr();
		printcl(";");
	}

	// is current token what we want?
    private boolean is(TK tk) {
        return tk == tok.kind;
    }

    // ensure current token is tk and skip over it.
    private void mustbe(TK tk) {
	if( tok.kind != tk ) {
	    System.err.println( "mustbe: want " + tk + ", got " +
				    tok);
	    parse_error( "missing token (mustbe)" );
	}
	scan();
    }

    private void parse_error(String msg) {
	System.err.println( "can't parse: line "
			    + tok.lineNumber + " " + msg );
	System.exit(1);
    }
}
