grammar MiniJaja;

@header {
  import org.antlr.v4.runtime.Token;
}

//==============================================
// PARSER
//==============================================
prog: 'class' ident '{' decls methmain '}';
ident:    IDENT;
decls:    (decl ';')*;
decl:     method #MethodDecl
        | var #VarDecl
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
        | 'write' '(' (ident1 | STRING ) ')'                  #Write
        | 'writeln' '(' (ident1 | STRING ) ')'                #Writeln
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
fact:     ( 'true' | 'false')    #Bool
        | ident1                 #VarRef
        | ident '(' listexp ')'  #MethodCall
        | 'length' '(' ident ')' #Length
        | NUMBER                 #Int
        | '(' exp ')'            #ParenExp
        ;
ident1:   ident             #Identificator
        | ident '[' exp ']' #ArrayAccess //ident ('[' exp ']')*; //to handle array access in
        ;
typemeth: 'void' #VoidType
        | type   #TypeMeth
        ;
type:     'int'      #IntType
        | 'boolean' #BoolType
        ;

//==============================================
// LEXER
//==============================================
fragment DIGIT:    [0-9];
NUMBER:   DIGIT+;
fragment LETTER:   [a-zA-Z];

IDENT:    (LETTER|'_')(LETTER|NUMBER|'_')*;
STRING: '"' (~["\\\r\n] | '\\' .)* '"';

LINE_COMMENT: '//' ~[\r\n]* -> skip;
BLOCK_COMMENT: '/*' .*? '*/' -> skip;

WS:       [ \t\r\n]+ -> skip;
