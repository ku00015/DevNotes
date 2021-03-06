package ku00015.devnotes;

import android.text.Editable;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;

import java.util.ArrayList;

public class Lib {

    /*
     * From current point (int start) in Editable s,
     * returns the index at which the new line starts ( + 1 ).
     * returns 0 if there hasn't been a new line.
     */
    public static int getLineStartIndex(Editable s, int start){
        return s.toString().lastIndexOf('\n', start) + 1; // +1 Lands you AFTER \n and if there is no \n it lands you at index=0
    }

    /*
     * From the start of the line (int lineStartIndex)
     */
    public static int getNumSpacesOfLine(Editable s, int lineStartIndex){
        String sub = s.toString().substring(lineStartIndex);
        int c = 0;
        for(int i = 0; i < sub.length(); i++){
            if(sub.charAt(i) == ' '){
                c++;
            } else{
                break;
            }
        }
        return c;
    }

    /*
     * From the current position, gets previous line's tabbing/ spacing length and creates a String of spaces
     * to match the length of spaces.
     */
    public static String getSpacesOfPrevLine(Editable s, int pos){
        String spaces = "";
        for(int i = 0; i < Lib.getNumSpacesOfLine(s, Lib.getLineStartIndex(s, pos - 1)); i++) spaces = spaces + " ";
        return spaces;
    }

    /*
     * Auto-Colour VALUES FROM A LIST:
     *
     * Iterate through ArrayList values to see if there are any occurances of each value,
     * if there are set a colour span on Editable s.
     *
     * No return needed:- Editable s is passed by pointer, object reference's values are changed within.
     */
    public static void autoColour(Editable s, ArrayList<String> arr, int colourID, String regexPrev, String regexNext){
        for(int i = 0; i < arr.size(); i++) {
            String word = arr.get(i);
            if(s.toString().contains(word)){
                int c = s.toString().indexOf(word);
                while (c >= 0) {
                    // ONE CHAR BEFORE WORD
                    if(c == 0 || (c > 0 && String.valueOf(s.charAt(c - 1)).matches(regexPrev))) {
                        // ONE CHAR AFTER WORD
                        if(c + word.length() >= s.length() || (c + word.length() < s.length() && String.valueOf(s.charAt(c + word.length())).matches(regexNext))) {
                                ForegroundColorSpan colour = new ForegroundColorSpan(colourID);
                                s.setSpan(colour, c, c + word.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        }
                    }
                    c = s.toString().indexOf(word, c + 1);
                }
            }
        }
    }

    /*
     * Auto-Colour QUOTATIONS/ STRINGS:
     *
     * If number of occurances of " is more than 1, Loop by getting quotation indexes and setting colour on them,
     * update occurances left (take away the two you just colourised) and do this until occurances is less than 2.
     */
    public static void autoColour(Editable s, int colourID){
        int c = s.toString().split("\"").length - 1;
        if(c > 1){
            int firstQuote = s.toString().indexOf("\"", 0);
            while(c > 1){
                if(firstQuote >= 0){
                    int secondQuote = s.toString().indexOf("\"", firstQuote + 1);
                    if(secondQuote >= 0){
                        ForegroundColorSpan quoteColor = new ForegroundColorSpan(colourID);
                        s.setSpan(quoteColor, firstQuote, secondQuote + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        firstQuote = s.toString().indexOf("\"", secondQuote + 1);
                    }
                }
                c = c - 2;
            }
        }
    }

}
