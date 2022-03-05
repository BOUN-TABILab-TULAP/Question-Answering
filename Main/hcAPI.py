#!flask/bin/python
from flask import Flask, request, jsonify, make_response, abort
import cPickle as pickle
import time

from main import *


import sys
reload(sys)
sys.setdefaultencoding('utf-8')

app = Flask(__name__)

#app.run(threaded=True)


# WE SHOULD RECORD THESE
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
		'summaries': [],
		'transDocs': [],
		'done': False
		}
	]

question = []

@app.route('/hc-api/v0.1/test', methods=['GET'])
def get_questions():
	return jsonify({'questions':questions})


@app.route('/hc-api/v0.1/mass', methods=['POST'])
def mass_evaluate():
	print("HC_API: Running Mass Evaluator")
	if not request.json or not 'evalDataFile' in request.json:
		abort(400)

	dataPath = request.json['evalDataFile']
	topDocsNum = int(request.json['topDocs']) # we don't use it in mass, right?

	# READ DATA FILE (to fill these lists:)

	questionList, focusListAnnotatedT, modListAnnotatedT, classListAnnotated, parsedBefore, answerList = mainReadDataFile(dataPath)

	# RUN the SYSTEM on EVERY QUESTION (to fill these lists:)

	#total = len(questionList) # just for debugging
	# ['dummy']*total

	
	if topDocsNum == 1 or "cogTest.data" in dataPath:
		bypassDocs=False
	else:
		bypassDocs=True

	bypassDocs=False
	bypassTrans=True

	focusListFoundT, modListFoundT, classListFound, transPhraseList, transList, relatedDocs, answerFoundInDocs, queries, subjects, clirDocs = mainEval(questionList, answerList, parsedBefore, dataPath, topDocs=topDocsNum, bypassDocs=bypassDocs, bypassTrans=bypassTrans)
	# format of relatedDocs -> [[titles, texts], [titles, texts], ...]

	print("HC_API: Compiling response ...")
	responseData = {
		'questionTexts': questionList,
		'answerList': answerList,
		'focusListAnnoT': focusListAnnotatedT,
		'focusListFoundT': focusListFoundT,
		'modListAnnoT': modListAnnotatedT,
		'modListFoundT': modListFoundT,
		'classListAnno': classListAnnotated,
		'classListFound': classListFound,
		'subjects': subjects,
		'transList': transList,
		'relatedDocs': relatedDocs,
		'answerFoundDocs': answerFoundInDocs,
		'queries': queries,
		'clirDocs': clirDocs
		}

	print("HC_API: Posting response...")
	return jsonify({'responseData':responseData}), 201
	

@app.route('/hc-api/v0.1/hazircevap', methods=['POST'])
def pilot_question():
	api = "HC_API PILOT: "
	print(api + "Running Single")
	if not request.json or not 'questionText' in request.json:
		abort(400)

	qText = request.json['questionText']


	titles, summaries, transTitles, transTexts = runPipeline(qText)

	transSummaries = mainSummarize(qText, "dumm ans", transTitles, transTexts, 3)

	print(api + "Recording Question : " + qText)

	with open('pilotQuestions', 'a') as recordFile:
		recordFile.write(time.strftime("%d/%m/%Y - %H:%M:%S") + '\t' + qText + '\n')

	print(api + "Compiling response...")
	question = {
		'qText': qText,
		'qRelated_doc_titles': titles,
		'summaries': summaries,
		'transTitles': transTitles,
		'transTexts': transTexts,
		'done': True
		}

	questions.append(question)

	print(api + "Posting response...")
	return jsonify({'question':question}), 201


@app.route('/hc-api/v0.1/test', methods=['POST'])
def post_question():
	api = "HC_API Single: "
	print(api + "Running Single")
	if not request.json or not 'questionText' in request.json:
		abort(400)

	print("\n\n\n=================\n\n")
	#print(request.json['topDocs'])
	print("\n\n\n=================\n\n")

	qText = request.json['questionText']

	topDocs = 10 #int(request.json['topDocs'])

	howManyDocsToSummarize = int(request.json['summaryTopDocs'])

	customQuery = request.json['customQuery']

	if customQuery is not None:
		#print(api + "query type : " + str(type(customQuery)))
		#print(api + "query input : < " + str(customQuery) + " >")

		# BEWARE : we do not validate the query input, it may be malformed
		queryInput = customQuery
	else:
		#print("QUERY NONE")
		queryInput = False
		
	
	qObj = mainParse(qText)

	qFoc, qFocRoots, qMod, qClass, qPnoun, qSubj = mainAnalyze(qObj, False, False)

	qTerms = mainBuildQuery(qObj, paramFile="singleFromWeb", queryInput=queryInput)

	docs = mainQuerySingle(count=topDocs)

	titles, texts = mainRelated(docs)

	print("Summarize DOCS : " + str(howManyDocsToSummarize))
	summaries = mainSummarize(qObj.questionText, "how could we possibly know the answer at this step", titles, texts, howManyDocsToSummarize)
	print("LENGTH summaries : " + str(len(summaries)))

	transPhrase = " ".join([ qPnoun, qMod + " " + qFoc ])
	#translation = mainTranslate(transPhrase)
	print(qText)
	transDocs = mainTranslateGoogle(qText)

	if transDocs == []:
		transDocs.append("See STDERR")
	#print(str(len(transDocs)))
	#print(transDocs)

	translation = "Bypassed (Google API is on the way)"

	

	print(api + "Compiling response...")
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
		'summaries': summaries,
		'transDocs': transDocs,
		'done': True
		}

	questions.append(question)

	print(api + "Posting response...")
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
