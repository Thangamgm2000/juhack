from flask import Flask, request, render_template,jsonify       # Flask libraries
from werkzeug import secure_filename                            # Saving files
import sqlite3 as sql                                           # Database
import os
import math,random                                              # Generating OTP
import smtplib                                                  # Sending email
import hashlib                                                  # Hashing passwords and OTP

# Separate folders for separate files
LICENSE_FOLDER = r'E:\General\Personal\Hackathon\Jain University Hack 19\Flask\LPD_flask\license'
INSURANCE_FOLDER = r'E:\General\Personal\Hackathon\Jain University Hack 19\Flask\LPD_flask\insurance'
RCBOOK_FOLDER = r'E:\General\Personal\Hackathon\Jain University Hack 19\Flask\LPD_flask\rcbook'

#UPLOAD_FOLDER = 'E:/General/Personal/Hackathon/Jain University Hack 19/Flask/LPD_flask/'
ALLOWED_EXTENSIONS = set(['png', 'jpg', 'jpeg'])        # Allowed extensions

app = Flask(__name__,template_folder="templates")
#app.config['UPLOAD_FOLDER'] = UPLOAD_FOLDER
app.config['LICENSE_FOLDER'] = LICENSE_FOLDER
app.config['INSURANCE_FOLDER'] = INSURANCE_FOLDER
app.config['RCBOOK_FOLDER'] = RCBOOK_FOLDER

def allowed_file(filename):
    return '.' in filename and \
        filename.rsplit('.', 1)[1].lower() in ALLOWED_EXTENSIONS

@app.route("/")
def home():
    return render_template('lpd_home.html')

# Add new record by filling the form
@app.route('/new')
def new_student():
    return render_template('lpd_input.html')

# Save the data and insert into Database
@app.route('/addrec',methods=['POST','GET'])    
def addrec():
    if request.method == 'POST':
        try:
            regno = request.form['regno']
            name = request.form['name']
            chassis = request.form['chassis']
            engine = request.form['engine']
            regdate = request.form['regdate']
            vehicle = request.form['vehicle']
            fuel = request.form['fuel']
            maker = request.form['maker']
            fitnessdate = request.form['fitnessdate']
            roadtaxdate = request.form['roadtaxdate']
            license = request.files['license']
            insurance = request.files['insurance']
            rcbook = request.files['rcbook']
            license_file = license.filename
            insurance_file = insurance.filename
            rcbook_file = rcbook.filename

            if license_file == '' and insurance_file == '' and rcbook_file == '':
                flash('No selected file')
                return redirect(request.url)
            if license_file and insurance_file and rcbook_file and allowed_file(license_file) and allowed_file(insurance_file) and allowed_file(rcbook_file):
                licensename = secure_filename(license_file)
                insurancename = secure_filename(insurance_file)
                rcbookname = secure_filename(rcbook_file)
                #flash('file {} saved'.format(file.filename))
                #LICENSE_FOLDER = UPLOAD_FOLDER +'/license'
                #INSURANCE_FOLDER = UPLOAD_FOLDER + '/insurance'
                #RCBOOK_FOLDER = UPLOAD_FOLDER + './rcbook'
                LICENSE_FOLDER = os.path.join(app.config['LICENSE_FOLDER'], licensename)
                INSURANCE_FOLDER = os.path.join(app.config['INSURANCE_FOLDER'], insurancename)
                RCBOOK_FOLDER = os.path.join(app.config['RCBOOK_FOLDER'], rcbookname)
                license.save(LICENSE_FOLDER)
                insurance.save(INSURANCE_FOLDER)
                rcbook.save(RCBOOK_FOLDER)
                print(LICENSE_FOLDER,"\n",INSURANCE_FOLDER,"\n",RCBOOK_FOLDER)
            with sql.connect('vehicle.db') as conn:
                cur = conn.cursor()
                #print("inserting")
                cur.execute('INSERT INTO VEHICLE(REGNO,NAME,CHASSIS,ENGINE,REGDATE,VEHICLE,FUEL,MAKER,FITNESS,ROADTAX,LICENSE,INSURANCE,RCBOOK) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)',(regno,name,chassis,engine,regdate,vehicle,fuel,maker,fitnessdate,roadtaxdate,LICENSE_FOLDER,INSURANCE_FOLDER,RCBOOK_FOLDER))
                #print("hello")
                conn.commit()
                msg = "Record added successfully"
         
        except:
            conn.rollback()
            msg = "Error while inserting"
        finally:
            return render_template('lpd_result.html',msg = msg)
            conn.close()
    
 # Display the records   
@app.route("/list")
def list():
    conn = sql.connect('vehicle.db')
    conn.row_factory = sql.Row      # Converts plain tuple into more useful row

    cur = conn.cursor()
    cur.execute("SELECT * FROM VEHICLE")
    
    rows = cur.fetchall()       # Fetches all the details in the row
    print()
    return render_template("lpd_list.html",rows = rows)

# Find the tuples with REGNO
@app.route("/find")
def find():
    return render_template('lpd_find.html')

# Finding the values with REGNO using JSON input API
@app.route("/finding",methods=['GET','POST'])
def finding():
    if(request.method == 'POST'):
        try:
            f = request.get_json(force = True)               # Get JSON data from App
            find = f['plate']                                # Look for needed data
            #find = str(request.form['find'])
            conn = sql.connect("vehicle.db")
            conn.row_factory = dict_factory
            cur = conn.cursor()
            cur.execute('SELECT * FROM VEHICLE WHERE REGNO=?',(find,))
            recs = cur.fetchall()
            print(recs)
            msg = recs
            conn.commit()
            #print(jsonify(msg[0]))
            return jsonify(msg[0])  # Return the data in JSON format

        except:
            conn.rollback()
            msg = "Error in finding"
        '''finally:
            return render_template("lpd_result.html",msg=msg)
            conn.close()'''
            
def dict_factory(cur, row):                         # Converting into JSON format
    d = {}
    for idx, col in enumerate(cur.description):
        d[col[0]] = row[idx]
    return d

def generateOTP():                                      # Generate OTP with 4 digits
    digits = "0123456789"
    OTP = ""

    for i in range(4):
        OTP += digits[math.floor(random.random()*10)]
    return OTP

@app.route("/credentials",methods=['GET','POST'])       # Validate the credentials
def credentials():
    if request.method == 'POST':
        try:
            f = request.get_json(force=True)
            username = f['username']
            password = f['password']
            conn = sql.connect('credentials.db')
            cur = conn.cursor()
            cur.execute("SELECT PASSWORD FROM CREDENTIALS WHERE USERNAME=?",(username,))
            recs = cur.fetchone()
            print(recs[0])
            d={}
            
            if(recs[0]==password):
                d = {'flag': 'success'}
                
            else:
                d = {'flag': 'failure'}
            print(d)
            return jsonify(d)                               # Jsonify and send the result of validation
        except:
            return "Error"

otp_g = ""
@app.route("/email",methods=['GET','POST'])                 # Validate the email ID and send the generated OTP to the specified email ID
def email():
    global otp_g
    if request.method == 'POST':
        try:
            f = request.get_json(force=True)
            email = f['email']
            conn = sql.connect('vehicle.db')
            cur = conn.cursor()
            cur.execute("SELECT EMAIL FROM VEHICLE WHERE EMAIL=?",(email,))
            recs = cur.fetchone()
            print(str(recs[0]))
            d={}
            
            if(recs[0]==email):
                d = {'flag': 'success'}
                print(d)
                s = smtplib.SMTP('smtp.gmail.com',587)
                s.starttls()
                s.login('thehawkblack00@gmail.com','phantom19')
                otp1 = generateOTP()
                otp_g = hashlib.sha256(otp1.encode())
                
                message =  "Hello sent email from TheBlackHawk. The OTP is "+ otp1
                print(message)
                s.sendmail("thehawkblack00@gmail.com","srishilesh@gmail.com",message)
                print("Sent email")
                s.quit()
            else:
                d = {'flag': 'failure'}
            print(d)
            return jsonify(d)
        except:
            return "Error"

@app.route('/otp_verify',methods=['GET','POST'])            # Verify the OTP received in the email
def otp_verify():
    global otp_g
    if request.method == 'POST':
        try:
            f = request.get_json(force=True)
            otp = f['otp']
            print("otp_g ",str(otp_g.hexdigest()))
            #conn = sql.connect('vehicle.db')
            #cur = conn.cursor()
            #cur.execute("SELECT EMAIL FROM VEHICLE WHERE EMAIL=?",(email,))
            #recs = cur.fetchone()
            #print(str(recs[0]))
            d={}
            
            if(str(otp_g.hexdigest())==(otp)):          # Convert the Hash object to string for comparison
                d = {'flag': 'success'}
            else:
                d = {'flag': 'failure'}
            print(d)
            return jsonify(d)
        except:
            return "Error"


if(__name__=='__main__'):
    app.run(debug=True)



