#!flask/bin/python
from flask import Flask, request, jsonify, make_response, abort

from main import *

import sys
reload(sys)
sys.setdefaultencoding('utf-8')

app = Flask(__name__)

questions = [
	{
		'qId': 1,
		'qText': 'Dummy Question',
		'qParts': [],
		'qFocus': [],
		'qFocRoots': [],
		'qMod': [],
		'qClass': [],
		'qPnoun': [],
		'qSubj': [],
		'qTransPhrase': [],
		'qTranslation': [],
		'qQuery': [],
		'qRelated_doc_ids': [],
		'qRelated_doc_titles': [],
		'qRelated_doc_texts': [],
		'done': False
		}
	]

question = []

@app.route('/hc-api/v0.1/test', methods=['GET'])
def get_questions():
	return jsonify({'questions':questions})


@app.route('/hc-api/v0.1/mass', methods=['POST'])
def mass_evaluate():
	if not request.json or not 'evalDataFile' in request.json:
		abort(400)

	dataFile = request.json['evalDataFile']
	topDocs = int(request.json['topDocs']) # we don't use it in mass, right?

	mainReadDataFile()

	# READ DATA FILE (to fill these lists:)

	questionList = []
	focusListAnnotatedT = []
	modListAnnotatedT = []
	classListAnnotated = []
	

	# RUN the SYSTEM on EVERY QUESTION (to fill these lists:)

	focusListFoundT = []
	modListFoundT = []
	classListFound = []
	transPhraseList = []
	transList = []

	responseData = {
		'questionTexts': questionList,
		'focusListAnnoT': focusListAnnotatedT,
		'focusListFoundT': focusListFoundT,
		'modListAnnoT': modListAnnotatedT,
		'modListFoundT': modListFoundT,
		'classListAnno': classListAnnotated,
		'classListFound': classListFound,
		'transPhraseList': transPhraseList,
		'transList': transList
			}

	return jsonify({'responseData':responseData}), 201
	

@app.route('/hc-api/v0.1/test', methods=['POST'])
def post_question():
	if not request.json or not 'questionText' in request.json:
		abort(400)

	print("\n\n\n=================\n\n")
	#print(request.json['topDocs'])
	print("\n\n\n=================\n\n")

	qText = request.json['questionText']

	topDocs = int(request.json['topDocs'])

	qObj = mainParse(qText)

	qFoc, qFocRoots, qMod, qClass, qPnoun, qSubj = mainAnalyze(qObj)

	qTerms = mainBuildQuery(qObj)

	docs = mainQuerySingle(count=topDocs)

	titles, texts = mainRelated(docs)

	transPhrase = " ".join([ qPnoun, qMod, qFocRoots ])
	translation = mainTranslate(transPhrase)

	question = {
		'qId': questions[-1]['qId'] + 1,
		'qText': qObj.questionText,
		'qParts': qObj.questionParts,
		'qFocus': qFoc,
		'qFocRoots': qFocRoots,
		'qMod': qMod,
		'qClass': qClass,
		'qPnoun': qPnoun,
		'qSubj': qSubj,
		'qTransPhrase': transPhrase,
		'qTranslation': translation,
		'qQuery': str(qTerms),
		'qRelated_doc_ids': docs,
		'qRelated_doc_titles': titles,
		'qRelated_doc_texts': texts,
		'done': True
		}

	questions.append(question)

	return jsonify({'question':question}), 201

"""
@app.route('/')
def index():
        return "Hello, World!"
"""

@app.errorhandler(404)
def not_found(error):
    return make_response(jsonify({'error': 'Not found'}), 404)

if __name__ == '__main__':
        app.run(debug=True)
