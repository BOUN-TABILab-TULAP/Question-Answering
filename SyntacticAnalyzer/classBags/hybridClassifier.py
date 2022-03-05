# -*- coding: utf-8 -*-

import codecs, operator

import sys
reload(sys)
sys.setdefaultencoding('utf-8')

def coarseFinder(question):

    qClass = False

    temporalPhrases = ['ne zaman', 
                       'hangi tarihte', 
                       'tarihi nedir', 
                       'hangi yüzyıl',
                       'kuruluş yılı', 
                       'hangi jeolojik ', 
                       'hangi çağ', 
                       'hangi yıl', 
                       'kaç yılında', 
                       'kaçıncı yüzyıl', 
                       'hangi mevsim', 
                       'hangi tarih',
                       'kuruluş yılı nedir']

    locationPhrases = ['hangi ülke', # 16
                       'hangi il', # 10
                       'nerede', # 15
                       'hangi bölge', # 14
                       'nereye', # 3
                       'hangi kıta', # 5
                       'nere', # 17
                       'bölge hangisidir', # 5
                       'kıta hangisidir', # 3
                       'ülke hangisidir', # 3
                       'bölgesi hangisidir', # 3
                       'kıtası hangisidir', # 2
                       'şehir hangisidir', # 1
                       'ilinin adı', # 2
                       'şehri', # 3
                       ]

    abbreviationPhrases = ['açılımı', 'kısa', 'ne demektir']

    humanPhrases = ['kim ', 'kimdir', 'kimin', 
                    'dili ', 
                    'hangi topluluk', 
                    'hangi uygarlık',
                    'hangi padişah',
                    'hangi kültür',
                    'uygarlık hangisidir']

    numericPhrases = ['kaç', 'kadardır']

    descriptionPhrases = ['ne denir', 
                          'ne ad verilir', 
                          'ne isim verilir',
                          'sonucu nedir', 
                          'sonuç nedir', 
                          'faktör', 
                          'sebebi nedir', 
                          'temel nedeni', 
                          'neye',
                          'neyi verir', 
                          'ne elde edilir']


    t = question.questionText

    """
    TEMPORAL
    """
    if not qClass:
        for tPhrase in temporalPhrases:
            if tPhrase.decode('utf-8') in t:
                qClass = 'TEMPORAL'
                break

    """
    LOCATION
    """
    if not qClass:
        for lPhrase in locationPhrases:
            if lPhrase.decode('utf-8') in t:
                qClass = 'LOCATION'
                break

    """
    ABBREVIATION
    """
    if not qClass:
        for aPhrase in abbreviationPhrases:
            if aPhrase.decode('utf-8') in t:
                qClass = 'ABBREVIATION'
                break

    """
    HUMAN
    """
    if not qClass:
        for hPhrase in humanPhrases:
            if hPhrase.decode('utf-8') in t:
                qClass = 'HUMAN'
                break

    """
    NUMERIC
    """
    if not qClass:
        for nPhrase in numericPhrases:
            if nPhrase.decode('utf-8') in t:
                qClass = 'NUMERIC'
                break

    """
    DESCRIPTION
    """
    if not qClass:
        for dPhrase in descriptionPhrases:
            if dPhrase.decode('utf-8') in t:
                qClass = 'DESCRIPTION'
                break

    if not qClass:
        qClass = 'ENTITY'

    return qClass
