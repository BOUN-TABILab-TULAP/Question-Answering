FROM openjdk:11
RUN apt update -y && apt install wget  -y 
RUN apt install python -y
RUN wget https://bootstrap.pypa.io/pip/2.7/get-pip.py
RUN python get-pip.py
WORKDIR /app
COPY . .
RUN  pip install -r requirements.txt
EXPOSE 3030
RUN wget -q http://nlp.cmpe.boun.edu.tr/staticFiles/hazircevap-ir.tar.gz
RUN mkdir IR && tar -xzf hazircevap-ir.tar.gz -C IR/ --strip-components=1
WORKDIR /app/Main
ENTRYPOINT ["python"]
CMD ["hc-api.py"]
# CMD tail -f /dev/null