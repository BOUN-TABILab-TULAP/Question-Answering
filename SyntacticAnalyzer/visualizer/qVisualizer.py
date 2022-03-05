# -*- coding: utf-8 -*-
import codecs, os


def visualizeAllQuestions(questions):

    htmlStr = '<html><head><meta http-equiv="Content-Type" content="text/html;charset=UTF-8"></head><body><ul>'

    qNumber = 1
    for question in questions:
        
        htmlStr += '<li><a href=\"visual' + str(qNumber) + '.html\">' + question.questionText + '</a></li>'

        produceVisualPage(question.questionParts, question.questionText, qNumber, True)
        qNumber += 1

    htmlStr += '</ul></body></html>'

    with codecs.open('visualizer/d3/all/index.html', 'w+', 'utf-8') as f:
        f.write(htmlStr)

def produceVisualPage(parts, qText, qNumber = 1, qAll = False):

    if qAll:
        if not os.path.exists('visualizer/d3/all/'):
            print('not exists, created')
            os.makedirs('visualizer/d3/all/')

        dirPath = 'visualizer/d3/all/'
    else:
        dirPath = 'visualizer/d3/'

    fName = dirPath + 'visual' + str(qNumber) + '.html'

    with codecs.open(fName, 'w+', 'utf-8') as f:
        f.write(htmlTextSingleQuestion(parts, qText, qAll))

# takes the question parts as array
def scriptTextSingleQuestion(parts):

    scriptStr = """
\n<script id="js">                                                             
    var g = new dagreD3.graphlib.Graph().setGraph({}).setDefaultEdgeLabel(function(){return {}; });"""


    # adding nodes
    for part in parts:
        partID = part[0]
        partText = part[1]

        # this is for deriv parts like : _ ne (goes to nedir) if
        if partText == "_":
            partText = part[2]

        scriptStr += "\n g.setNode(" + partID + ", {label: \"" + partText + "\",class: \"type-TK\"});"


    # adding edges
    for part in parts:
        partID = part[0]
        partRoot = part[6]

        partDepTag = part[7] # like SUBJECT

        if part[1] == ".":
            continue

        scriptStr += "\n g.setEdge(" + partID + "," + partRoot + "," + "{label: \"" + partDepTag + "\"});"

    scriptStr += """
  var render = new dagreD3.render();

// Set up an SVG group so that we can translate the final graph.
var svg = d3.select("svg"),
    svgGroup = svg.append("g");

// Run the renderer. This is what draws the final graph.
render(d3.select("svg g"), g);

// Center the graph
var xCenterOffset = (svg.attr("width") - g.graph().width) / 2;
svgGroup.attr("transform", "translate(" + xCenterOffset + ", 20)");
svg.attr("height", g.graph().height + 40);

d3.select("svg").call(d3.behavior.zoom().on("zoom", function() {
  var svg = d3.select("svg");
  var ev = d3.event;
  svg.select("g")
  .attr("transform", "translate(" + ev.translate + ") scale(" + ev.scale + ")");
  }));
</script>
"""
    return scriptStr




def htmlTextSingleQuestion(parts, qText, qAll):
    htmlStr = """<meta charset=utf-8">\n
<script src="http://d3js.org/d3.v3.min.js" charset="utf-8"></script>\n"""

    htmlStr += "<script type=\"text/javascript\" src=\""
    if qAll:
        htmlStr += '../resources/dagre-d3.js\"></script>'
    else:
        htmlStr += 'resources/dagre-d3.js\"></script>'

    htmlStr += """

<style id="css">
g.type-TK > rect {
  fill: #00ffd0;
}

text {
  font-weight: 300;
  font-family: "Helvetica Neue", Helvetica, Arial, sans-serf;
  font-size: 14px;
}

.node rect {
  stroke: #999;
  fill: #fff;
  stroke-width: 1.5px;
}

.edgePath path {
  stroke: #333;
  stroke-width: 1.5px;
}
</style>

<p>%s</p>

<hr>

<svg id="svg-canvas" width=960 height=600 style="border: 1px solid #ccc;"></svg>

""" % (qText)

    htmlStr += scriptTextSingleQuestion(parts)

    return htmlStr
