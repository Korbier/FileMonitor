def i = 0;

if ( event.equals("ENTRY_CREATE") ) print ( i + " [+] ");
if ( event.equals("ENTRY_DELETE") ) print ( i + " [-] ");
if ( event.equals("ENTRY_MODIFY") ) print ( i + " [ ] ");

print path + "\\" + file + "\n"
i++;