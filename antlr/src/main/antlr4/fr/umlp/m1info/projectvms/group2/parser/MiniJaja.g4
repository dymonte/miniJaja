grammar MiniJaja;

@header {
  import org.antlr.v4.runtime.Token;
}

// Declaration of grammar rules
class:    'class' ident '{' decls methmain '}';
ident:    IDENT;
decls:    (decl ';')*;
decl:     var    #VarDecl
        | method #MethodDecl
        ;
vars:     (var ';')*;
var:      typemeth ident vexp        #VarDeclSimple 
        | typemeth ident '[' exp ']' #VarDeclArray
        | 'final' type ident vexp    #VarDeclFinal
        ;
vexp:     ('=' exp)? ;
method:   typemeth ident '(' params ')' '{' vars instrs '}';
methmain: 'main' '{' vars instrs '}';
params:   (param (',' param)*)? ;
param:    type ident;
instrs:   (instr ';')* ;
instr:    ident1 op=('='|'+='|'-='|'*='|'/=') exp              #Assignment
        | ident1 op=('++'|'--')                                #Increment
        | ident '(' listexp ')'                                #CallI
        | 'return' exp                                         #Return
        | 'write' '(' (ident1 | '"'STRING?'"' ) ')'                  #Write
        | 'writeln' '(' (ident1 | '"'STRING?'"' ) ')'                #Writeln
        | 'if' '('exp')' '{'instrs'}' ('else' '{' instrs '}')? #If
        | 'while' '('exp')''{'instrs'}'                        #While
        ;
listexp : (exp (',' exp)*)?;
exp:      '!'exp1                 #NotExp
        | exp op=('&&'|'||') exp1 #LogicExp
        | exp1                    #SingleExp1
        ;
exp1:     exp1 op=('=='|'!='|'<'|'>'|'<='|'>=') exp2 #CompExp
        | exp2                                       #Exp2Only
        ;
exp2:     exp2 op=('+'|'-') term #AddSubExp
        | '-' term               #NegExp
        | term                   #TermOnly
        ;
term:     term op=('*'|'/'|'%') fact #MulDivExp
        | fact                       #FactOnly
        ;
fact:		  ident1                 #VarRef
        | ident '(' listexp ')'  #MethodCall
        | 'length' '(' ident ')' #Length
        | BOOLEAN                #Bool
        | NUMBER                 #Int
        | '(' exp ')'            #ParenExp
        ;
ident1:   ident             #Identificator
        | ident '[' exp ']' #ArrayAccess
        ;
typemeth: 'void' #VoidType
        | type   #TypeMeth
        ;
type:     'int'      #IntType
        | 'boolean' #BoolType
        ;

// ident1:  IDENT ('[' exp ']')* ; // to handle array access in

// Declaration of identifiers and types
DIGIT:    [0-9];
NUMBER:   DIGIT+;
LETTER:   [a-zA-Z];
STRING:   [\u0000-\u007F\u00C0-\u00FF]+;
WS:       [ \t\r\n]+ -> skip;

IDENT:    (LETTER|'_')(LETTER|NUMBER|'_'|'-')*;
TRUE:     'true';
FALSE:    'false';
BOOLEAN:  TRUE|FALSE;
VOID:     'void';
