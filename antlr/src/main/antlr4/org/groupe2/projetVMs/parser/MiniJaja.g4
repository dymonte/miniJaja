grammar MiniJaja;

@header {
  import org.antlr.v4.runtime.Token;
}

// Declaration of grammar rules
class:    'class' ident '{' decls methmain '}';
ident:    IDENT;
decls:    decl ';' decls
        | ;//(decl ';')*;
decl:     var | method;
vars:     var ';' vars
        | ;//(var ';')*;
var:      typemeth ident vexp 
        | typemeth ident '[' exp ']'
        | 'final' type ident vexp;
vexp:     ('=' exp)? ;
method:   typemeth ident '(' params ')' '{' vars instrs '}';
methmain: 'main' '{' vars instrs '}';
params:   param ',' params
        | param
        | ;//(param (',' param)*)? ;
param:    type ident;
instrs:   instr ';' instrs
        | ;//(instr ';')* ;
instr:    ident1 op=('='|'+='|'-='|'*='|'/=') exp #Assignment
        | ident1 op=('++'|'--') #Increment
        | ident1 '(' listexp ')'
        | 'return' exp #Return
        | 'write' '(' (ident1 | STRING? ) ')'
        |;





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
