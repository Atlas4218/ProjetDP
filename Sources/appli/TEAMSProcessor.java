package appli;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class TEAMSProcessor {

    private Collection<People> _allpeople = null;
    private String _fileName;
    private String _startTime;
    private String _endTime;
	private String _cours;
	private String _date;

    public TEAMSProcessor(File _file, String _intitule, String _date, String _start, String _stop, String comparator) {
        /*
         csv file to read
         start time of the course
         end time of the source
        */
        this._startTime = _start;
        this._endTime = _stop;
        this._date = _date;
        this._cours = _intitule;

        // load CSV file
        this._fileName = _file.getName();
        var teamsFile = new TEAMSAttendanceList(_file);

        // filter to extract data for each people
        var lines = teamsFile.get_attlist();
        if (lines != null) {
            // convert lines in data structure with people & periods
            var filter = new TEAMSAttendanceListAnalyzer(lines);
            // cut periods before start time and after end time
            filter.setStartAndStop(_start, _stop);
            // sort
            List<People> people= new ArrayList<>(filter.get_peopleList(comparator).values());
            Collections.sort(people);
            // init the people collection
            this._allpeople = people;//filter.get_peopleList().values();
        }
    }

    public Collection<People> get_allpeople() {
        return _allpeople;
    }

    public String toHTMLCode(boolean name, boolean id, boolean planning) {
        String html = "<!DOCTYPE html> \n <html lang=\"fr\"> \n <head> \n <meta charset=\"utf-8\"> ";
        html += "<title> Attendance Report </title> \n <link rel=\"stylesheet\" media=\"all\" href=\"visu.css\"> \n";
        html += "</head> \n <body> \n ";
        html += "<h1> Rapport de connexion </h1>\n" +
                "\n" +
                "<div id=\"blockid\">\n" +
                "<table>\n" +
                "\t<tr>\n" +
                "\t\t<th> Date : </th>\n" +
                "\t\t<td> " + this._date + " </td>\n" +
                "\t</tr>\n" +
                "\t<tr>\n" +
                "\t\t<th> Heure d??but : </th>\n" +
                "\t\t<td> " + this._startTime.split(" ?? ")[1] + " </td>\n" +
                "\t</tr>\n" +
                "\t<tr>\n" +
                "\t\t<th> Heure fin : </th>\n" +
                "\t\t<td> " + this._endTime.split(" ?? ")[1] + " </td>\n" +
                "\t</tr>\n" +
                "\t<tr>\n" +
                "\t\t<th> Cours : </th>\n" +
                "\t\t<td> "+this._cours+" </td>\n" +
                "\t</tr>\n" +
                "\t<tr>\n" +
                "\t\t<th> Fichier analys?? : </th>\n" +
                "\t\t<td> " + this._fileName + " </td>\n" +
                "\t</tr>\n" +
                "\t<tr>\n" +
                "\t\t<th> Nombre de connect??s : </th>\n" +
                "\t\t<td> " + this._allpeople.size() + "  </td>\n" +
                "\t</tr>\n" +
                "</table>\n" +
                "</div>\n" +
                "\n" +
                "<h2> Dur??es de connexion</h2>\n" +
                "\n" +
                "<p> Pour chaque personne ci-dessous, on retrouve son temps total de connexion sur la plage d??clar??e du cours, ainsi qu'un graphe qui indique les p??riodes de connexion (en vert) et d'absence de connexion (en blanc). En pointant la souris sur une zone, une bulle affiche les instants de d??but et de fin de p??riode. \n" +
                "</p>";
        html += "<div id=\"blockpeople\"> ";

        for (People people : this._allpeople) {

            html += people.getHTMLCode(name, id, planning);
        }

	    html += "</div> \n </body> \n </html>";
        return html;
    }
}
