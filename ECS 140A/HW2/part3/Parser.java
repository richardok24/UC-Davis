import java.util.LinkedList;

/* *** This file is given as part of the programming assignment. *** */

public class Parser {

	private Symbol_Table symbolTable  = new Symbol_Table(); // call symboltable class to have a symboltable to modify.
    // tok is global to all these parsing methods;
    // scan just calls the scanner's scan method and saves the result in tok.
    private Token tok;// the current token
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

    private void program() {
	block();
    }

    private void block(){
    //System.out.println(symbolTable.size());
    symbolTable.enter(new LinkedList<String>());//when block up, a new list enter.
	declaration_list();
	statement_list();
	symbolTable.exit();//when block done, the list done.
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
	if(is(TK.ID)) {
		symbolTable.checkorAdd(tok.string);
		scan();
	}
	else mustbe(TK.ID); // can use mustbe to exit and print error, it cant be right when in the else case :).
	
	while( is(TK.COMMA) ) {
	    scan();
		if(is(TK.ID)) {
			symbolTable.checkorAdd(tok.string);
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
		guarded_command();
		
		while(is(TK.ELSEIF)) {
			scan();
			guarded_command();
		}
		
		if(is(TK.ELSE)) {
			scan();
			block();
		}
		mustbe(TK.ENDIF);	
	}

	private void guarded_command() {
		// TODO Auto-generated method stub
		expr();
		mustbe(TK.THEN);
		block();
	}

	private void expr() {
		// TODO Auto-generated method stub
		term();
		while(isAddop()) {
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
			scan();
			factor();
		}
	}

	private void factor() {
		// TODO Auto-generated method stub
		if(is(TK.LPAREN)) {
			scan();
			expr();
			mustbe(TK.RPAREN);
		} 
		else if(isRef_id()) ref_id();
		else mustbe(TK.NUM);
	}

	private void ref_id() {
		// TODO Auto-generated method stub
		if(is(TK.REF)) {
			int num = -1; // global scope;
			scan();
			if(is(TK.NUM)) {
				num = Integer.parseInt(tok.string);
				scan();//get var after"~"
			}
			if(num == -1) {
				if(!symbolTable.checkGloabl(tok.string)) {
					System.err.print("no such variable ~" + tok.string+" on line " + tok.lineNumber + "\n");
					System.exit(1);
				}
			}
			else{
				if(!symbolTable.checkthelvl(tok.string,num)){
					System.err.print("no such variable ~" + num 
						+ tok.string+" on line " + tok.lineNumber + "\n");
					System.exit(1);
				}				
			}				
		}
		else {
			if(!symbolTable.search(tok.string)) {	
				System.err.print(tok.string + " is an undeclared variable on line " + tok.lineNumber + "\n");			
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
		guarded_command();
		mustbe(TK.ENDDO);
	}

	private void PT_case() {
		// TODO Auto-generated method stub
		mustbe(TK.PRINT);
		expr();
	}

	private void assignment() {
		// TODO Auto-generated method stub
		ref_id();
		mustbe(TK.ASSIGN);
		expr();
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
