/*
 * Copyright (C) 2017 dbcurtis
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.dbcrd.DBCUtilLib;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Stream;
import static net.dbcrd.DBCUtilLib.Lambdas.EMPTY_STRING;
import static net.dbcrd.DBCUtilLib.Lambdas.IS_EMPTY;

/**
 * <strong>Class to return a Matcher whos group() will return all the text between the<tt> &lt;B&gt; and &lt;/B&gt; </tt>tags</strong>
 *
 * @author dbcurtis
 */
@ClassPreamble(author = "Daniel B. Curtis",
    date = "March 2017 ",
    currentRevision = 1,
       lastModified = "04/01/2017",
    lastModifiedBy = "Daniel B. Curtis")
public class HTMLTagContent
{

    /**
     *
     */
    private static final List<String> TAGS =Arrays.asList("!DOCTYPE" ,"a" ,"abbr" ,"acronym" ,"address" ,"applet" ,"area" ,"article" ,
           "aside" ,"audio" ,"b" ,"base" ,"basefont" ,"bdi" ,"bdo" ,"big" ,"blockquote" ,"body" ,"br" ,"button" ,"canvas" ,
           "caption" ,"center" ,"cite" ,"code" ,"col" ,"colgroup" ,"command" ,"datalist" ,"dd" ,"del" ,"details" ,"dfn" ,
           "dir" ,"div" ,"dl" ,"dt" ,"em" ,"embed" ,"fieldset" ,"figcaption" ,"figure" ,"font" ,"footer" ,"form" ,"frame", 
           "frameset" ,"h1" ,"h2" ,"h3" ,"h4" ,"h5" ,"h6" ,"head" ,"header" ,"hgroup" ,"hr" ,"html" ,"i" ,"iframe" ,"img",
           "input" ,"ins" ,"kbd" ,"keygen" ,"label" ,"legend" ,"li" ,"link" ,"map" ,"mark" ,"menu" ,"meta" ,"meter" ,"nav" ,
           "noframes" ,"noscript" ,"object" ,"ol" ,"optgroup" ,"option" ,"output" ,"p" ,"param" ,"pre" ,"progress" ,"q" ,
           "rp" ,"rt" ,"ruby" ,"s" ,"samp" ,"script" ,"section" ,"select" ,"small" ,"source" ,"span" ,"strike" ,"strong" ,
           "style" ,"sub" ,"summary" ,"sup" ,"table" ,"tbody" ,"td" ,"textarea" ,"tfoot" ,"th" ,"thead" ,"time" ,"title" ,
           "tr" ,"track" ,"tt" ,"u" ,"ul" ,"var" ,"video" ,"wbr" );

    /** 
     * REGEX template for extracting text surrounded by Tags such as &lt;tag&gt;text&lt;/tag&gt; 
     * it does not work for &lt;tag ss="junk"&gt;text&lt;/tag&gt; 
     */
    private static final String BASE_REG = "" //modified slightly from the REGEX book
            // + "<B>" //here the tag is "B"
            + "<B.*?>" //here the tag is "B"
            + "(?>[^<]*)"
            + "(?>"
            + "(?!</?B>)"
            + "<"
            + "[^<]*"
            + ")*"
            + "</B>";
    /** error pattern */
    private static final Pattern JUNKP = Pattern.compile("JUNK");
    /**
     * THE LOGGER
     */
    private static final Logger THE_LOGGER = Logger.getLogger(HTMLTagContent.class.getName());

    /** returns a Stream of Stream that are the legal values for tagIn */
    public static final Supplier <Stream<String>> LEGAL_TAGS =()->TAGS.stream();
    
    /**
     *
     */
    private Optional<Pattern> oP;

    /**
     *
     */
    private final String tag;
    /**
     * This functional returns the tag matcher if defined, else a matcher for JUNK
     */
    public final Supplier<Matcher> matcher = () -> oP.map(p ->p.matcher(EMPTY_STRING))
        .orElse(JUNKP.matcher(EMPTY_STRING));
/**
 * Setup a matcher for an HTML tag.
 * 
 * @param tagIn a String, must not be null or empty after trimming.  This is a HTML tag,
 * @param bargs a boolean vararg that if it exists disables the unknown HTML tag check
 * any list of tags, The tag can consist of letters and numbers 1-6
 */
    public HTMLTagContent(final String tagIn, final boolean... bargs )
    {
        this.tag = Objects.requireNonNull(tagIn).trim().toLowerCase(Locale.getDefault());
        if (IS_EMPTY.test(this.tag)){
            throw new IllegalArgumentException("argument cannot be empty");
        }
        if(!this.tag.matches("[a-z1-6]+")){
            throw new IllegalArgumentException("argument must be alpha and 1-6 only");
        }
        if (!TAGS.contains(this.tag)&&bargs.length==0){
             throw new IllegalArgumentException(this.tag+" is an unknown HTML tag");
        }
              
        try{
        oP= Optional.ofNullable(
                Pattern.compile(BASE_REG.replaceAll("B", this.tag),Pattern.CASE_INSENSITIVE));
        }catch(PatternSyntaxException pse){
            oP=Optional.empty();
            THE_LOGGER.log(Level.SEVERE, "{0} was illegal to replace B in BASE_REG", tag);
        }
    }
    
    
    @Override
    public boolean equals(Object o){
        if (this==o){return true;}
        if (o instanceof HTMLTagContent){
            final HTMLTagContent that =(HTMLTagContent)o;
            return this.oP.get().pattern().equals(that.oP.get().pattern());
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.oP);
        return hash;
    }
    @Override
    public String toString(){
        return tag;
    }
    
}
