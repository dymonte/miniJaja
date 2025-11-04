grammar JajaCode;

@header {
	import org.antlr.v4.runtime.Token;
}

//=================================================
// PARSER
//=================================================
prog
    : instr '\n' prog
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
    : 'int' | 'boolean' | 'void' | 'meth'
    ;

kind
    : 'var' | 'cst' | 'arr'
    ;

//======================================
// LEXER
//======================================
IDENTIFIER : [a-zA-Z_][a-zA-Z0-9]* ;
NUMBER     : [0-9]+ ;
WS         : [ \t\r]+ -> skip;
COMMENT    : '//' ~[\r\n]* -> skip;
