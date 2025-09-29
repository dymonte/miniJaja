grammar MiniJaja;

@header {
  import org.antlr.v4.runtime.Token;
}

// Declaration of grammar rules
class:    'class' ident '{' decls methmain '}';
ident:    IDENT;
decls:    (decl ';')*;
decl:     var | method;
vars:     (var ';')*;
var:      typemeth ident vexp 
        | typemeth ident '[' exp ']'
        | 'final' type ident vexp
        ;
vexp:     ('=' exp)? ;
method:   typemeth ident '(' params ')' '{' vars instrs '}';
methmain: 'main' '{' vars instrs '}';
params:   (param (',' param)*)? ;
param:    type ident;
instrs:   (instr ';')* ;
instr:    ident1 op=('='|'+='|'-='|'*='|'/=') exp #Assignment
        | ident1 op=('++'|'--') #Increment
        | ident1 '(' listexp ')' #CallI
        | 'return' exp #Return
        | 'write' '(' (ident1 | STRING? ) ')' #Write
        | 'writeln' '(' (ident1 | STRING? ) ')' #Writeln
        | 'if' '('exp')' '{'instrs'}' ('else' '{' instrs '}')? #If
        | 'while' '('exp')''{'instrs'}' #While
        |	#Void
        ;
listexp : exp ',' listexp
        | exp
        |
        ;
exp:      '!'exp1
        | exp op=('&&'|'||')exp1
        | exp1
        ;
exp1:    exp1 op=('=='|'!='|'<'|'>'|'<='|'>=') exp2
        | exp2
        ;
exp2:    exp2 op=('+'|'-') term
        | term
        ;
term:    term op=('*'|'/'|'%') fact
        | fact
        ;
fact:		 ident1
        | ident '(' listexp ')'
        | 'length' '(' ident ')'
        | BOOLEAN
        | NUMBER
        ;
ident1:   ident
        | ident '[' exp ']'
        ;
typemeth: 'void'
        | type
        ;
type:     'int' | 'boolean';

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
