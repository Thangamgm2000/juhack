from imutils import contours
import numpy as np
import imutils
import cv2

rectKernel = cv2.getStructuringElement(cv2.MORPH_RECT,(9,3))
sqrKernel = cv2.getStructuringElement(cv2.MORPH_RECT,(5,5))

img = "test.jpg"
image = cv2.imread(img)
image = imutils.resize(image, width = 300)
image1 = cv2.imread(img)
image1 = imutils.resize(image1, width = 300)

gray = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)
cv2.imshow("frame1",gray)

tophat = cv2.morphologyEx(gray, cv2.MORPH_TOPHAT, rectKernel)

cv2.imshow("frame2",tophat)

gradX = cv2.Sobel(tophat, ddepth=cv2.CV_32F, dx=1, dy=0, ksize=-1)
gradX = np.absolute(gradX)
(minVal,maxVal) = (np.min(gradX),np.max(gradX))
gradX = (255 * ((gradX - minVal) / (maxVal - minVal)))
gradX = gradX.astype("uint8")
cv2.imshow("frame3",gradX)

thresh = cv2.morphologyEx(gradX, cv2.MORPH_CLOSE, rectKernel)
cv2.imshow("frame5",gradX)
thresh = cv2.adaptiveThreshold(gradX,150,cv2.ADAPTIVE_THRESH_GAUSSIAN_C,cv2.THRESH_BINARY,11,2)
thresh = cv2.threshold(gradX, 0 , 255, cv2.THRESH_BINARY |  cv2.THRESH_OTSU)[1]
cv2.imshow("frame6",thresh)
thresh = cv2.morphologyEx(thresh, cv2.MORPH_CLOSE, rectKernel)
thresh = cv2.morphologyEx(thresh, cv2.MORPH_GRADIENT,sqrKernel)
cv2.imshow("frame7",thresh)

cnts = cv2.findContours(thresh.copy(),cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)
cnts = cnts[0] if imutils.is_cv2() else cnts[1]

locs = []

free = []

for (i,c) in enumerate(cnts):
    (x,y,w,h) = cv2.boundingRect(c)

    ar = w/float(h)

    if ar>2 and ar<10:
        locs.append((x,y,w,h))
    free.append((x,y,w,h))
for lx in locs:


    x,y,w,h = lx

    cv2.rectangle(image,(x,y),(x+w+10,y+h+10),(164,18,63),2)

for lx in free:
    x,y,w,h = lx

    cv2.rectangle(image1, (x,y), (x+w+10, y+h+10), (164,18,63),2)


cv2.imshow("Specificframe",image)
cv2.imshow("AllFrame", image1)
cv2.waitKey(0)
cv2.destroyAllWindows()
