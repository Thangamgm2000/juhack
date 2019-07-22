import sqlite3
import json
conn = sqlite3.connect('vehicle.db')

#conn.execute("CREATE TABLE VEHICLE(REGNO TEXT,NAME TEXT,CHASSIS TEXT,ENGINE TEXT,REGDATE TEXT,VEHICLE TEXT,FUEL TEXT,\
#        MAKER TEXT,FITNESS TEXT,ROADTAX TEXT,LICENSE TEXT,INSURANCE TEXT,RCBOOK TEXT)")
'''
def dict_factory(cursor, row):
    d = {}
    for idx, col in enumerate(cursor.description):
        d[col[0]] = row[idx]
    return d

conn.row_factory = dict_factory
'''
#cursor = conn.cursor()

#cursor.execute("select * from vehicle where regno=?",("MH12DE1433"))

# fetch all or one we'll go for all.

#results = cursor.fetchall()

#print(results)
#conn.execute("UPDATE VEHICLE SET EMAIL='srishilesh@gmail.com' WHERE REGNO='MH12DE1433'")
#connection.close()
#conn.execute("DROP TABLE VEHICLE")
#cur = conn.cursor()
#cur.execute('INSERT INTO VEHICLE(REGNO,NAME,CHASSIS,ENGINE,REGDATE,VEHICLE,FUEL,MAKER,FITNESS,ROADTAX,LICENSE,INSURANCE,RCBOOK) VALUES("1234","avc","234","343","12/12/2012","asdf","asdfgf","sdfsdfsd","23/9/2009","29/4/2000","hi","bye","hello")')
conn.commit()
print("Success")
conn.close()