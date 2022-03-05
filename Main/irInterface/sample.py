# -*- coding: utf-8 -*-

from indriHandler import singleIndriQuery
from queryBuilder import buildIndriQuerySingle
from indriDocFetch import getDoc

terms = ['Richter','ölç','ölçeği','amaç','amaçla','kullan','kullanılır']

buildIndriQuerySingle(1, terms)

returnedDocs = singleIndriQuery(1)

print("\nReturned Docs: " + str(returnedDocs) + "\n")

print(getDoc(returnedDocs[0]))

for doc in returnedDocs:
    print(getDoc(doc))
