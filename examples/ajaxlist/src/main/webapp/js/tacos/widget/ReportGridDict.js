// Supported languages - English and German
dojo.provide("tacos.widget.ReportGridDict");

var DICT = {};
DICT["en"] = {};
DICT["de"] = {};
DICT["ru"] = {};

DICT["en"]["greaterequal"] = "Greater than or equal to";
DICT["en"]["lessequal"] = "Less than or equal to";
DICT["en"]["equal"] = "Equals";
DICT["en"]["notequal"] = "Not Equals";
DICT["en"]["starts"] = "Starts with";
DICT["en"]["contains"] = "Contains";
DICT["en"]["endswith"] = "Ends with";
DICT["en"]["filter"] = "FILTER";
DICT["en"]["clear"] = "CLEAR";
DICT["en"]["to"] = "to";
DICT["en"]["of"] = "of";
DICT["en"]["page"] = "Page";
DICT["en"]["all"] = "All";

DICT["de"]["greaterequal"] = "ist grösser oder gleich";
DICT["de"]["lessequal"] = "ist weniger oder gleich";
DICT["de"]["equal"] = "ist genau";
DICT["de"]["notequal"] = "entspricht nicht";
DICT["de"]["starts"] = "beginnt mit";
DICT["de"]["contains"] = "enthält";
DICT["de"]["endswith"] = "endet mit";
DICT["de"]["filter"] = "FILTER";
DICT["de"]["clear"] = "ZURÜCKSETZEN";
DICT["de"]["to"] = "bis";
DICT["de"]["of"] = "von";
DICT["de"]["page"] = "Seite";
DICT["de"]["all"] = "Alle";


DICT["ru"]["greaterequal"] = "Больше или равно";
DICT["ru"]["lessequal"] = "Меньше или равно";
DICT["ru"]["equal"] = "Равно";
DICT["ru"]["notequal"] = "Не равно";
DICT["ru"]["starts"] = "Начинается с";
DICT["ru"]["contains"] = "Содержится";
DICT["ru"]["endswith"] = "Заканчивается на";
DICT["ru"]["filter"] = "ПРИМЕНИТЬ";
DICT["ru"]["clear"] = "ОЧИСТИТЬ";
DICT["ru"]["to"] = "по";
DICT["ru"]["of"] = "из";
DICT["ru"]["page"] = "Страница";
DICT["ru"]["all"] = "Все";


tacos.widget.ReportGrigDict=function(){};

var DEF_LANG = "en";
var UNDEFINED = "undefined"
translate = function(language, key){
	 if (key==null) return UNDEFINED;
	 if (language==null) language = DEF_LANG;
	 var value = DICT[language][key];
	 if (value==null) value = DICT[DEF_LANG][key];
	 if (value==null) value = "[" + key + "]";
	 return value;
};







