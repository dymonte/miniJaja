grammar JajaCode;

@header {
	import org.antlr.v4.runtime.Token;
}

//=================================================
// PARSER
//=================================================
prog
    : instr (NEWLINE instr)* NEWLINE? EOF
    |
    ;

instr
    : 'init'                 #Init
    | 'swap'                 #Swap
    | 'new' '(' ident ',' type ',' kind ',' address ')' #New
    | 'newarray' '(' ident ',' type ')'                 #NewArray
    | 'invoke' '(' ident ')' #Invoke
    | 'length' '(' ident ')' #Length
    | 'return'               #Return
    | 'write'                #Write
    | 'writeln'              #WriteLn
    | 'push' '(' value ')'   #Push
    | 'pop'                  #Pop
    | 'load' '(' ident ')'   #Load
    | 'aload' '(' ident ')'  #ALoad
    | 'store' '(' ident ')'  #Store
    | 'astore' '(' ident ')' #AStore
    | 'if' '(' address ')'    #If
    | 'goto' '(' address ')'  #Goto
    | 'inc' '(' ident ')'    #Inc
    | 'ainc' '(' ident ')'   #AInc
    | oper                   #Operator
    | 'nop'                  #Nop
    | 'jcstop'               #JcStop
    ; 

oper
    : oper1 #Unary
    | oper2 #Binary
    ;

oper1
    : 'neg' | 'not'
    ;

oper2
    : 'add' | 'sub' | 'mul' | 'div' | 'cmp' | 'sup' | 'or' | 'and'
    ;

ident : IDENTIFIER ;

value  
    : NUMBER             #IntValue
    | ('true' | 'false') #BoolValue
    | STRING             #StringValue
    |                    #VoidValue
    ;

address : NUMBER ;
type
    : 'int' | 'boolean' | 'void'
    ;

kind
    : 'var' | 'cst' | 'arr' | 'meth'
    ;

fragment CHARS: (~["\\\r\n] | '\\' .)*;
STRING: '"' CHARS '"' { String text = getText(); setText(text.substring(1, text.length() -1)); };

//======================================
// LEXER
//======================================
IDENTIFIER : [a-zA-Z_][a-zA-Z0-9]* ;
NUMBER     : [0-9]+ ;
NEWLINE    : '\r'? '\n';
WS         : [ \t]+ -> skip;
COMMENT    : '//' ~[\r\n]* -> skip;
