// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

package avtas.util;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

/**
 * Aids in the creation and manipulation of Color objects. This class defines
 * retrieves colors based on standard HTML color names (see
 * http://www.w3schools.com).
 *
 * <TABLE class=reference>
 *
 * <TBODY>
 * <TR>
 * <TH width="25%" align=left>Color Name</TH>
 * <TH width="15%" align=left>HEX</TH>
 * <TH width="43%" align=left>Color</TH>
 * <TH width="11%" align=left>Shades</TH>
 * <TH align=left>Mix</TH></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=AliceBlue"
 * target=_blank>AliceBlue</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=F0F8FF"
 * target=_blank>#F0F8FF</A></TD>
 * <TD bgColor=#f0f8ff>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=F0F8FF">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=F0F8FF&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=AntiqueWhite"
 * target=_blank>AntiqueWhite</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=FAEBD7"
 * target=_blank>#FAEBD7</A></TD>
 * <TD bgColor=#faebd7>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=FAEBD7">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=FAEBD7&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=Aqua"
 * target=_blank>Aqua</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=00FFFF"
 * target=_blank>#00FFFF</A></TD>
 * <TD bgColor=#00ffff>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=00FFFF">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=00FFFF&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=Aquamarine"
 * target=_blank>Aquamarine</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=7FFFD4"
 * target=_blank>#7FFFD4</A></TD>
 * <TD bgColor=#7fffd4>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=7FFFD4">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=7FFFD4&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=Azure"
 * target=_blank>Azure</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=F0FFFF"
 * target=_blank>#F0FFFF</A></TD>
 * <TD bgColor=#f0ffff>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=F0FFFF">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=F0FFFF&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=Beige"
 * target=_blank>Beige</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=F5F5DC"
 * target=_blank>#F5F5DC</A></TD>
 * <TD bgColor=#f5f5dc>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=F5F5DC">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=F5F5DC&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=Bisque"
 * target=_blank>Bisque</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=FFE4C4"
 * target=_blank>#FFE4C4</A></TD>
 * <TD bgColor=#ffe4c4>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=FFE4C4">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=FFE4C4&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=Black"
 * target=_blank>Black</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=000000"
 * target=_blank>#000000</A></TD>
 * <TD bgColor=#000000>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=000000">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=000000&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=BlanchedAlmond"
 * target=_blank>BlanchedAlmond</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=FFEBCD"
 * target=_blank>#FFEBCD</A></TD>
 * <TD bgColor=#ffebcd>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=FFEBCD">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=FFEBCD&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=Blue"
 * target=_blank>Blue</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=0000FF"
 * target=_blank>#0000FF</A></TD>
 * <TD bgColor=#0000ff>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=0000FF">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=0000FF&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=BlueViolet"
 * target=_blank>BlueViolet</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=8A2BE2"
 * target=_blank>#8A2BE2</A></TD>
 * <TD bgColor=#8a2be2>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=8A2BE2">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=8A2BE2&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=Brown"
 * target=_blank>Brown</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=A52A2A"
 * target=_blank>#A52A2A</A></TD>
 * <TD bgColor=#a52a2a>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=A52A2A">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=A52A2A&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=BurlyWood"
 * target=_blank>BurlyWood</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=DEB887"
 * target=_blank>#DEB887</A></TD>
 * <TD bgColor=#deb887>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=DEB887">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=DEB887&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=CadetBlue"
 * target=_blank>CadetBlue</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=5F9EA0"
 * target=_blank>#5F9EA0</A></TD>
 * <TD bgColor=#5f9ea0>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=5F9EA0">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=5F9EA0&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=Chartreuse"
 * target=_blank>Chartreuse</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=7FFF00"
 * target=_blank>#7FFF00</A></TD>
 * <TD bgColor=#7fff00>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=7FFF00">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=7FFF00&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=Chocolate"
 * target=_blank>Chocolate</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=D2691E"
 * target=_blank>#D2691E</A></TD>
 * <TD bgColor=#d2691e>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=D2691E">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=D2691E&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=Coral"
 * target=_blank>Coral</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=FF7F50"
 * target=_blank>#FF7F50</A></TD>
 * <TD bgColor=#ff7f50>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=FF7F50">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=FF7F50&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=CornflowerBlue"
 * target=_blank>CornflowerBlue</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=6495ED"
 * target=_blank>#6495ED</A></TD>
 * <TD bgColor=#6495ed>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=6495ED">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=6495ED&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=Cornsilk"
 * target=_blank>Cornsilk</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=FFF8DC"
 * target=_blank>#FFF8DC</A></TD>
 * <TD bgColor=#fff8dc>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=FFF8DC">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=FFF8DC&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=Crimson"
 * target=_blank>Crimson</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=DC143C"
 * target=_blank>#DC143C</A></TD>
 * <TD bgColor=#dc143c>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=DC143C">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=DC143C&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=Cyan"
 * target=_blank>Cyan</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=00FFFF"
 * target=_blank>#00FFFF</A></TD>
 * <TD bgColor=#00ffff>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=00FFFF">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=00FFFF&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=DarkBlue"
 * target=_blank>DarkBlue</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=00008B"
 * target=_blank>#00008B</A></TD>
 * <TD bgColor=#00008b>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=00008B">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=00008B&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=DarkCyan"
 * target=_blank>DarkCyan</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=008B8B"
 * target=_blank>#008B8B</A></TD>
 * <TD bgColor=#008b8b>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=008B8B">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=008B8B&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=DarkGoldenRod"
 * target=_blank>DarkGoldenRod</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=B8860B"
 * target=_blank>#B8860B</A></TD>
 * <TD bgColor=#b8860b>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=B8860B">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=B8860B&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=DarkGray"
 * target=_blank>DarkGray</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=A9A9A9"
 * target=_blank>#A9A9A9</A></TD>
 * <TD bgColor=#a9a9a9>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=A9A9A9">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=A9A9A9&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=DarkGreen"
 * target=_blank>DarkGreen</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=006400"
 * target=_blank>#006400</A></TD>
 * <TD bgColor=#006400>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=006400">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=006400&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=DarkKhaki"
 * target=_blank>DarkKhaki</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=BDB76B"
 * target=_blank>#BDB76B</A></TD>
 * <TD bgColor=#bdb76b>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=BDB76B">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=BDB76B&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=DarkMagenta"
 * target=_blank>DarkMagenta</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=8B008B"
 * target=_blank>#8B008B</A></TD>
 * <TD bgColor=#8b008b>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=8B008B">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=8B008B&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=DarkOliveGreen"
 * target=_blank>DarkOliveGreen</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=556B2F"
 * target=_blank>#556B2F</A></TD>
 * <TD bgColor=#556b2f>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=556B2F">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=556B2F&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=Darkorange"
 * target=_blank>Darkorange</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=FF8C00"
 * target=_blank>#FF8C00</A></TD>
 * <TD bgColor=#ff8c00>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=FF8C00">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=FF8C00&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=DarkOrchid"
 * target=_blank>DarkOrchid</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=9932CC"
 * target=_blank>#9932CC</A></TD>
 * <TD bgColor=#9932cc>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=9932CC">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=9932CC&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=DarkRed"
 * target=_blank>DarkRed</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=8B0000"
 * target=_blank>#8B0000</A></TD>
 * <TD bgColor=#8b0000>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=8B0000">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=8B0000&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=DarkSalmon"
 * target=_blank>DarkSalmon</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=E9967A"
 * target=_blank>#E9967A</A></TD>
 * <TD bgColor=#e9967a>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=E9967A">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=E9967A&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=DarkSeaGreen"
 * target=_blank>DarkSeaGreen</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=8FBC8F"
 * target=_blank>#8FBC8F</A></TD>
 * <TD bgColor=#8fbc8f>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=8FBC8F">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=8FBC8F&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=DarkSlateBlue"
 * target=_blank>DarkSlateBlue</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=483D8B"
 * target=_blank>#483D8B</A></TD>
 * <TD bgColor=#483d8b>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=483D8B">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=483D8B&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=DarkSlateGray"
 * target=_blank>DarkSlateGray</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=2F4F4F"
 * target=_blank>#2F4F4F</A></TD>
 * <TD bgColor=#2f4f4f>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=2F4F4F">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=2F4F4F&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=DarkTurquoise"
 * target=_blank>DarkTurquoise</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=00CED1"
 * target=_blank>#00CED1</A></TD>
 * <TD bgColor=#00ced1>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=00CED1">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=00CED1&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=DarkViolet"
 * target=_blank>DarkViolet</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=9400D3"
 * target=_blank>#9400D3</A></TD>
 * <TD bgColor=#9400d3>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=9400D3">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=9400D3&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=DeepPink"
 * target=_blank>DeepPink</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=FF1493"
 * target=_blank>#FF1493</A></TD>
 * <TD bgColor=#ff1493>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=FF1493">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=FF1493&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=DeepSkyBlue"
 * target=_blank>DeepSkyBlue</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=00BFFF"
 * target=_blank>#00BFFF</A></TD>
 * <TD bgColor=#00bfff>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=00BFFF">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=00BFFF&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=DimGray"
 * target=_blank>DimGray</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=696969"
 * target=_blank>#696969</A></TD>
 * <TD bgColor=#696969>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=696969">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=696969&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=DimGrey"
 * target=_blank>DimGrey</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=696969"
 * target=_blank>#696969</A></TD>
 * <TD bgColor=#696969>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=696969">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=696969&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=DodgerBlue"
 * target=_blank>DodgerBlue</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=1E90FF"
 * target=_blank>#1E90FF</A></TD>
 * <TD bgColor=#1e90ff>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=1E90FF">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=1E90FF&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=FireBrick"
 * target=_blank>FireBrick</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=B22222"
 * target=_blank>#B22222</A></TD>
 * <TD bgColor=#b22222>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=B22222">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=B22222&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=FloralWhite"
 * target=_blank>FloralWhite</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=FFFAF0"
 * target=_blank>#FFFAF0</A></TD>
 * <TD bgColor=#fffaf0>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=FFFAF0">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=FFFAF0&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=ForestGreen"
 * target=_blank>ForestGreen</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=228B22"
 * target=_blank>#228B22</A></TD>
 * <TD bgColor=#228b22>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=228B22">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=228B22&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=Fuchsia"
 * target=_blank>Fuchsia</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=FF00FF"
 * target=_blank>#FF00FF</A></TD>
 * <TD bgColor=#ff00ff>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=FF00FF">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=FF00FF&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=Gainsboro"
 * target=_blank>Gainsboro</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=DCDCDC"
 * target=_blank>#DCDCDC</A></TD>
 * <TD bgColor=#dcdcdc>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=DCDCDC">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=DCDCDC&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=GhostWhite"
 * target=_blank>GhostWhite</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=F8F8FF"
 * target=_blank>#F8F8FF</A></TD>
 * <TD bgColor=#f8f8ff>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=F8F8FF">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=F8F8FF&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=Gold"
 * target=_blank>Gold</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=FFD700"
 * target=_blank>#FFD700</A></TD>
 * <TD bgColor=#ffd700>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=FFD700">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=FFD700&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=GoldenRod"
 * target=_blank>GoldenRod</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=DAA520"
 * target=_blank>#DAA520</A></TD>
 * <TD bgColor=#daa520>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=DAA520">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=DAA520&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=Gray"
 * target=_blank>Gray</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=808080"
 * target=_blank>#808080</A></TD>
 * <TD bgColor=#808080>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=808080">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=808080&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=Green"
 * target=_blank>Green</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=008000"
 * target=_blank>#008000</A></TD>
 * <TD bgColor=#008000>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=008000">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=008000&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=GreenYellow"
 * target=_blank>GreenYellow</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=ADFF2F"
 * target=_blank>#ADFF2F</A></TD>
 * <TD bgColor=#adff2f>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=ADFF2F">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=ADFF2F&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=HoneyDew"
 * target=_blank>HoneyDew</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=F0FFF0"
 * target=_blank>#F0FFF0</A></TD>
 * <TD bgColor=#f0fff0>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=F0FFF0">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=F0FFF0&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=HotPink"
 * target=_blank>HotPink</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=FF69B4"
 * target=_blank>#FF69B4</A></TD>
 * <TD bgColor=#ff69b4>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=FF69B4">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=FF69B4&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=IndianRed"
 * target=_blank>IndianRed </A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=CD5C5C"
 * target=_blank>#CD5C5C</A></TD>
 * <TD bgColor=#cd5c5c>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=CD5C5C">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=CD5C5C&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=Indigo"
 * target=_blank>Indigo </A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=4B0082"
 * target=_blank>#4B0082</A></TD>
 * <TD bgColor=#4b0082>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=4B0082">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=4B0082&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=Ivory"
 * target=_blank>Ivory</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=FFFFF0"
 * target=_blank>#FFFFF0</A></TD>
 * <TD bgColor=#fffff0>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=FFFFF0">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=FFFFF0&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=Khaki"
 * target=_blank>Khaki</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=F0E68C"
 * target=_blank>#F0E68C</A></TD>
 * <TD bgColor=#f0e68c>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=F0E68C">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=F0E68C&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=Lavender"
 * target=_blank>Lavender</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=E6E6FA"
 * target=_blank>#E6E6FA</A></TD>
 * <TD bgColor=#e6e6fa>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=E6E6FA">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=E6E6FA&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=LavenderBlush"
 * target=_blank>LavenderBlush</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=FFF0F5"
 * target=_blank>#FFF0F5</A></TD>
 * <TD bgColor=#fff0f5>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=FFF0F5">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=FFF0F5&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=LawnGreen"
 * target=_blank>LawnGreen</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=7CFC00"
 * target=_blank>#7CFC00</A></TD>
 * <TD bgColor=#7cfc00>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=7CFC00">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=7CFC00&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=LemonChiffon"
 * target=_blank>LemonChiffon</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=FFFACD"
 * target=_blank>#FFFACD</A></TD>
 * <TD bgColor=#fffacd>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=FFFACD">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=FFFACD&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=LightBlue"
 * target=_blank>LightBlue</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=ADD8E6"
 * target=_blank>#ADD8E6</A></TD>
 * <TD bgColor=#add8e6>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=ADD8E6">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=ADD8E6&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=LightCoral"
 * target=_blank>LightCoral</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=F08080"
 * target=_blank>#F08080</A></TD>
 * <TD bgColor=#f08080>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=F08080">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=F08080&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=LightCyan"
 * target=_blank>LightCyan</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=E0FFFF"
 * target=_blank>#E0FFFF</A></TD>
 * <TD bgColor=#e0ffff>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=E0FFFF">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=E0FFFF&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=LightGoldenRodYellow"
 * target=_blank>LightGoldenRodYellow</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=FAFAD2"
 * target=_blank>#FAFAD2</A></TD>
 * <TD bgColor=#fafad2>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=FAFAD2">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=FAFAD2&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=LightGray"
 * target=_blank>LightGray</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=D3D3D3"
 * target=_blank>#D3D3D3</A></TD>
 * <TD bgColor=#d3d3d3>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=D3D3D3">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=D3D3D3&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=LightGreen"
 * target=_blank>LightGreen</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=90EE90"
 * target=_blank>#90EE90</A></TD>
 * <TD bgColor=#90ee90>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=90EE90">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=90EE90&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=LightPink"
 * target=_blank>LightPink</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=FFB6C1"
 * target=_blank>#FFB6C1</A></TD>
 * <TD bgColor=#ffb6c1>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=FFB6C1">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=FFB6C1&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=LightSalmon"
 * target=_blank>LightSalmon</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=FFA07A"
 * target=_blank>#FFA07A</A></TD>
 * <TD bgColor=#ffa07a>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=FFA07A">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=FFA07A&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=LightSeaGreen"
 * target=_blank>LightSeaGreen</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=20B2AA"
 * target=_blank>#20B2AA</A></TD>
 * <TD bgColor=#20b2aa>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=20B2AA">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=20B2AA&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=LightSkyBlue"
 * target=_blank>LightSkyBlue</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=87CEFA"
 * target=_blank>#87CEFA</A></TD>
 * <TD bgColor=#87cefa>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=87CEFA">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=87CEFA&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=LightSlateGray"
 * target=_blank>LightSlateGray</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=778899"
 * target=_blank>#778899</A></TD>
 * <TD bgColor=#778899>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=778899">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=778899&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=LightSteelBlue"
 * target=_blank>LightSteelBlue</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=B0C4DE"
 * target=_blank>#B0C4DE</A></TD>
 * <TD bgColor=#b0c4de>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=B0C4DE">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=B0C4DE&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=LightYellow"
 * target=_blank>LightYellow</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=FFFFE0"
 * target=_blank>#FFFFE0</A></TD>
 * <TD bgColor=#ffffe0>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=FFFFE0">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=FFFFE0&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=Lime"
 * target=_blank>Lime</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=00FF00"
 * target=_blank>#00FF00</A></TD>
 * <TD bgColor=#00ff00>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=00FF00">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=00FF00&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=LimeGreen"
 * target=_blank>LimeGreen</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=32CD32"
 * target=_blank>#32CD32</A></TD>
 * <TD bgColor=#32cd32>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=32CD32">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=32CD32&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=Linen"
 * target=_blank>Linen</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=FAF0E6"
 * target=_blank>#FAF0E6</A></TD>
 * <TD bgColor=#faf0e6>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=FAF0E6">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=FAF0E6&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=Magenta"
 * target=_blank>Magenta</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=FF00FF"
 * target=_blank>#FF00FF</A></TD>
 * <TD bgColor=#ff00ff>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=FF00FF">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=FF00FF&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=Maroon"
 * target=_blank>Maroon</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=800000"
 * target=_blank>#800000</A></TD>
 * <TD bgColor=#800000>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=800000">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=800000&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=MediumAquaMarine"
 * target=_blank>MediumAquaMarine</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=66CDAA"
 * target=_blank>#66CDAA</A></TD>
 * <TD bgColor=#66cdaa>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=66CDAA">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=66CDAA&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=MediumBlue"
 * target=_blank>MediumBlue</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=0000CD"
 * target=_blank>#0000CD</A></TD>
 * <TD bgColor=#0000cd>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=0000CD">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=0000CD&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=MediumOrchid"
 * target=_blank>MediumOrchid</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=BA55D3"
 * target=_blank>#BA55D3</A></TD>
 * <TD bgColor=#ba55d3>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=BA55D3">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=BA55D3&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=MediumPurple"
 * target=_blank>MediumPurple</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=9370DB"
 * target=_blank>#9370DB</A></TD>
 * <TD bgColor=#9370db>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=9370DB">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=9370DB&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=MediumSeaGreen"
 * target=_blank>MediumSeaGreen</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=3CB371"
 * target=_blank>#3CB371</A></TD>
 * <TD bgColor=#3cb371>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=3CB371">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=3CB371&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=MediumSlateBlue"
 * target=_blank>MediumSlateBlue</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=7B68EE"
 * target=_blank>#7B68EE</A></TD>
 * <TD bgColor=#7b68ee>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=7B68EE">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=7B68EE&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=MediumSpringGreen"
 * target=_blank>MediumSpringGreen</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=00FA9A"
 * target=_blank>#00FA9A</A></TD>
 * <TD bgColor=#00fa9a>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=00FA9A">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=00FA9A&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=MediumTurquoise"
 * target=_blank>MediumTurquoise</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=48D1CC"
 * target=_blank>#48D1CC</A></TD>
 * <TD bgColor=#48d1cc>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=48D1CC">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=48D1CC&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=MediumVioletRed"
 * target=_blank>MediumVioletRed</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=C71585"
 * target=_blank>#C71585</A></TD>
 * <TD bgColor=#c71585>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=C71585">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=C71585&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=MidnightBlue"
 * target=_blank>MidnightBlue</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=191970"
 * target=_blank>#191970</A></TD>
 * <TD bgColor=#191970>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=191970">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=191970&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=MintCream"
 * target=_blank>MintCream</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=F5FFFA"
 * target=_blank>#F5FFFA</A></TD>
 * <TD bgColor=#f5fffa>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=F5FFFA">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=F5FFFA&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=MistyRose"
 * target=_blank>MistyRose</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=FFE4E1"
 * target=_blank>#FFE4E1</A></TD>
 * <TD bgColor=#ffe4e1>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=FFE4E1">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=FFE4E1&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=Moccasin"
 * target=_blank>Moccasin</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=FFE4B5"
 * target=_blank>#FFE4B5</A></TD>
 * <TD bgColor=#ffe4b5>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=FFE4B5">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=FFE4B5&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=NavajoWhite"
 * target=_blank>NavajoWhite</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=FFDEAD"
 * target=_blank>#FFDEAD</A></TD>
 * <TD bgColor=#ffdead>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=FFDEAD">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=FFDEAD&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=Navy"
 * target=_blank>Navy</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=000080"
 * target=_blank>#000080</A></TD>
 * <TD bgColor=#000080>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=000080">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=000080&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=OldLace"
 * target=_blank>OldLace</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=FDF5E6"
 * target=_blank>#FDF5E6</A></TD>
 * <TD bgColor=#fdf5e6>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=FDF5E6">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=FDF5E6&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=Olive"
 * target=_blank>Olive</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=808000"
 * target=_blank>#808000</A></TD>
 * <TD bgColor=#808000>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=808000">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=808000&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=OliveDrab"
 * target=_blank>OliveDrab</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=6B8E23"
 * target=_blank>#6B8E23</A></TD>
 * <TD bgColor=#6b8e23>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=6B8E23">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=6B8E23&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=Orange"
 * target=_blank>Orange</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=FFA500"
 * target=_blank>#FFA500</A></TD>
 * <TD bgColor=#ffa500>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=FFA500">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=FFA500&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=OrangeRed"
 * target=_blank>OrangeRed</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=FF4500"
 * target=_blank>#FF4500</A></TD>
 * <TD bgColor=#ff4500>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=FF4500">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=FF4500&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=Orchid"
 * target=_blank>Orchid</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=DA70D6"
 * target=_blank>#DA70D6</A></TD>
 * <TD bgColor=#da70d6>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=DA70D6">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=DA70D6&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=PaleGoldenRod"
 * target=_blank>PaleGoldenRod</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=EEE8AA"
 * target=_blank>#EEE8AA</A></TD>
 * <TD bgColor=#eee8aa>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=EEE8AA">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=EEE8AA&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=PaleGreen"
 * target=_blank>PaleGreen</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=98FB98"
 * target=_blank>#98FB98</A></TD>
 * <TD bgColor=#98fb98>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=98FB98">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=98FB98&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=PaleTurquoise"
 * target=_blank>PaleTurquoise</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=AFEEEE"
 * target=_blank>#AFEEEE</A></TD>
 * <TD bgColor=#afeeee>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=AFEEEE">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=AFEEEE&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=PaleVioletRed"
 * target=_blank>PaleVioletRed</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=DB7093"
 * target=_blank>#DB7093</A></TD>
 * <TD bgColor=#db7093>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=DB7093">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=DB7093&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=PapayaWhip"
 * target=_blank>PapayaWhip</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=FFEFD5"
 * target=_blank>#FFEFD5</A></TD>
 * <TD bgColor=#ffefd5>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=FFEFD5">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=FFEFD5&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=PeachPuff"
 * target=_blank>PeachPuff</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=FFDAB9"
 * target=_blank>#FFDAB9</A></TD>
 * <TD bgColor=#ffdab9>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=FFDAB9">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=FFDAB9&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=Peru"
 * target=_blank>Peru</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=CD853F"
 * target=_blank>#CD853F</A></TD>
 * <TD bgColor=#cd853f>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=CD853F">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=CD853F&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=Pink"
 * target=_blank>Pink</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=FFC0CB"
 * target=_blank>#FFC0CB</A></TD>
 * <TD bgColor=#ffc0cb>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=FFC0CB">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=FFC0CB&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=Plum"
 * target=_blank>Plum</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=DDA0DD"
 * target=_blank>#DDA0DD</A></TD>
 * <TD bgColor=#dda0dd>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=DDA0DD">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=DDA0DD&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=PowderBlue"
 * target=_blank>PowderBlue</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=B0E0E6"
 * target=_blank>#B0E0E6</A></TD>
 * <TD bgColor=#b0e0e6>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=B0E0E6">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=B0E0E6&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=Purple"
 * target=_blank>Purple</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=800080"
 * target=_blank>#800080</A></TD>
 * <TD bgColor=#800080>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=800080">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=800080&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=Red"
 * target=_blank>Red</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=FF0000"
 * target=_blank>#FF0000</A></TD>
 * <TD bgColor=#ff0000>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=FF0000">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=FF0000&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=RosyBrown"
 * target=_blank>RosyBrown</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=BC8F8F"
 * target=_blank>#BC8F8F</A></TD>
 * <TD bgColor=#bc8f8f>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=BC8F8F">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=BC8F8F&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=RoyalBlue"
 * target=_blank>RoyalBlue</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=4169E1"
 * target=_blank>#4169E1</A></TD>
 * <TD bgColor=#4169e1>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=4169E1">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=4169E1&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=SaddleBrown"
 * target=_blank>SaddleBrown</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=8B4513"
 * target=_blank>#8B4513</A></TD>
 * <TD bgColor=#8b4513>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=8B4513">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=8B4513&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=Salmon"
 * target=_blank>Salmon</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=FA8072"
 * target=_blank>#FA8072</A></TD>
 * <TD bgColor=#fa8072>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=FA8072">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=FA8072&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=SandyBrown"
 * target=_blank>SandyBrown</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=F4A460"
 * target=_blank>#F4A460</A></TD>
 * <TD bgColor=#f4a460>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=F4A460">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=F4A460&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=SeaGreen"
 * target=_blank>SeaGreen</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=2E8B57"
 * target=_blank>#2E8B57</A></TD>
 * <TD bgColor=#2e8b57>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=2E8B57">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=2E8B57&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=SeaShell"
 * target=_blank>SeaShell</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=FFF5EE"
 * target=_blank>#FFF5EE</A></TD>
 * <TD bgColor=#fff5ee>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=FFF5EE">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=FFF5EE&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=Sienna"
 * target=_blank>Sienna</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=A0522D"
 * target=_blank>#A0522D</A></TD>
 * <TD bgColor=#a0522d>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=A0522D">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=A0522D&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=Silver"
 * target=_blank>Silver</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=C0C0C0"
 * target=_blank>#C0C0C0</A></TD>
 * <TD bgColor=#c0c0c0>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=C0C0C0">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=C0C0C0&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=SkyBlue"
 * target=_blank>SkyBlue</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=87CEEB"
 * target=_blank>#87CEEB</A></TD>
 * <TD bgColor=#87ceeb>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=87CEEB">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=87CEEB&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=SlateBlue"
 * target=_blank>SlateBlue</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=6A5ACD"
 * target=_blank>#6A5ACD</A></TD>
 * <TD bgColor=#6a5acd>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=6A5ACD">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=6A5ACD&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=SlateGray"
 * target=_blank>SlateGray</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=708090"
 * target=_blank>#708090</A></TD>
 * <TD bgColor=#708090>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=708090">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=708090&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=Snow"
 * target=_blank>Snow</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=FFFAFA"
 * target=_blank>#FFFAFA</A></TD>
 * <TD bgColor=#fffafa>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=FFFAFA">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=FFFAFA&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=SpringGreen"
 * target=_blank>SpringGreen</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=00FF7F"
 * target=_blank>#00FF7F</A></TD>
 * <TD bgColor=#00ff7f>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=00FF7F">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=00FF7F&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=SteelBlue"
 * target=_blank>SteelBlue</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=4682B4"
 * target=_blank>#4682B4</A></TD>
 * <TD bgColor=#4682b4>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=4682B4">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=4682B4&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=Tan"
 * target=_blank>Tan</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=D2B48C"
 * target=_blank>#D2B48C</A></TD>
 * <TD bgColor=#d2b48c>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=D2B48C">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=D2B48C&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=Teal"
 * target=_blank>Teal</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=008080"
 * target=_blank>#008080</A></TD>
 * <TD bgColor=#008080>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=008080">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=008080&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=Thistle"
 * target=_blank>Thistle</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=D8BFD8"
 * target=_blank>#D8BFD8</A></TD>
 * <TD bgColor=#d8bfd8>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=D8BFD8">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=D8BFD8&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=Tomato"
 * target=_blank>Tomato</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=FF6347"
 * target=_blank>#FF6347</A></TD>
 * <TD bgColor=#ff6347>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=FF6347">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=FF6347&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=Turquoise"
 * target=_blank>Turquoise</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=40E0D0"
 * target=_blank>#40E0D0</A></TD>
 * <TD bgColor=#40e0d0>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=40E0D0">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=40E0D0&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=Violet"
 * target=_blank>Violet</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=EE82EE"
 * target=_blank>#EE82EE</A></TD>
 * <TD bgColor=#ee82ee>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=EE82EE">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=EE82EE&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=Wheat"
 * target=_blank>Wheat</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=F5DEB3"
 * target=_blank>#F5DEB3</A></TD>
 * <TD bgColor=#f5deb3>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=F5DEB3">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=F5DEB3&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=White"
 * target=_blank>White</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=FFFFFF"
 * target=_blank>#FFFFFF</A></TD>
 * <TD bgColor=#ffffff>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=FFFFFF">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=FFFFFF&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=WhiteSmoke"
 * target=_blank>WhiteSmoke</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=F5F5F5"
 * target=_blank>#F5F5F5</A></TD>
 * <TD bgColor=#f5f5f5>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=F5F5F5">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=F5F5F5&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=Yellow"
 * target=_blank>Yellow</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=FFFF00"
 * target=_blank>#FFFF00</A></TD>
 * <TD bgColor=#ffff00>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=FFFF00">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=FFFF00&amp;colortop=FFFFFF">Mix</A></TD></TR>
 * <TR>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?color=YellowGreen"
 * target=_blank>YellowGreen</A>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_color_tryit.asp?hex=9ACD32"
 * target=_blank>#9ACD32</A></TD>
 * <TD bgColor=#9acd32>&nbsp;</TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colorpicker.asp?colorhex=9ACD32">Shades</A></TD>
 * <TD align=left><A
 * href="http://www.w3schools.com/tags/ref_colormixer.asp?colorbottom=9ACD32&amp;colortop=FFFFFF">Mix</A></TD></TR></TBODY></TABLE><BR>
 *
 * @author AFRL/RQQD
 */
public class Colors {

    /**
     * A clear color object (black, with alpha = 0)
     */
    public static final Color TRANSPARENT = new Color(0, 0, 0, 0);
    public static final Map<Integer, Color> colorCache = new HashMap<>();

    /**
     * Returns a new color that has the red, green, and blue components of the
     * base color, but has the user-specified alpha applied.
     *
     * @param base color on which new color is based.
     * @param alpha alpha value of color in the range [0..1]
     * @return a new color with the given alpha.
     */
    public static Color setAlpha(Color base, double alpha) {
        return new Color(base.getRed(), base.getGreen(), base.getBlue(), (int) (alpha * 255));
    }

    /**
     * Creates a blend of two colors. The ratio determines how much of the first
     * color is blended with the second.
     *
     * @param color1 The first color to blend
     * @param color2 The second color to blend
     * @param ratio a normalized value [0..1] setting the amount of color1 to
     * blend with color2
     * @return A new color that is a blend of red, green, blue, and alpha.
     */
    public static Color blend(Color color1, Color color2, double ratio) {
        double r2 = 1.0 - ratio;
        return new Color(
                (float) (color1.getRed() * ratio + color2.getRed() * r2),
                (float) (color1.getGreen() * ratio + color2.getGreen() * r2),
                (float) (color1.getBlue() * ratio + color2.getBlue() * r2),
                (float) (color1.getAlpha() * ratio + color2.getAlpha() * r2));
    }

    /**
     * Returns a color from the HTML color table or the color according to the
     * RGB value. If there is no color with the corresponding, label, this
     * returns the user-specified default color, which can be null.
     *
     * @param label String color name corresponding to the HTML color table, a series of
     * r,g,b,a values, or a hexadecimal color code.
     * @param defaultColor Default color if the color is not found. Can be null.
     *
     * @return The Color object.
     */
    public static Color getColor(String label, Color defaultColor) {

        if (label == null || label.isEmpty()) {
            return defaultColor;
        }

        // if the color is comma separated [r,g,b,a]...
        if (label.contains(",")) {
            String[] cvals = label.split(",");
            int r = 0, g = 0, b = 0, a = 0;
            if (cvals.length > 0) {
                r = Integer.valueOf(cvals[0]);
            }
            if (cvals.length > 1) {
                g = Integer.valueOf(cvals[1]);
            }
            if (cvals.length > 2) {
                b = Integer.valueOf(cvals[2]);
            }
            if (cvals.length > 3) {
                a = Integer.valueOf(cvals[3]);
            }
            return new Color(r, g, b, a);
        }

        
        // try to get an HTML-named color
        label = label.toLowerCase();
        switch (label) {

            case "aliceblue":
                return getColorFromCache(0xf0f8ff);
            case "antiquewhite":
                return getColorFromCache(0xfaebd7);
            case "aqua":
                return getColorFromCache(0x00ffff);
            case "aquamarine":
                return getColorFromCache(0x7fffd4);
            case "azure":
                return getColorFromCache(0xf0ffff);
            case "beige":
                return getColorFromCache(0xf5f5dc);
            case "bisque":
                return getColorFromCache(0xffe4c4);
            case "black":
                return getColorFromCache(0x000000);
            case "blanchedalmond":
                return getColorFromCache(0xffebcd);
            case "blue":
                return getColorFromCache(0x0000ff);
            case "blueviolet":
                return getColorFromCache(0x8a2be2);
            case "brown":
                return getColorFromCache(0xa52a2a);
            case "burlywood":
                return getColorFromCache(0xdeb887);
            case "cadetblue":
                return getColorFromCache(0x5f9ea0);
            case "chartreuse":
                return getColorFromCache(0x7fff00);
            case "chocolate":
                return getColorFromCache(0xd2691e);
            case "coral":
                return getColorFromCache(0xff7f50);
            case "cornflowerblue":
                return getColorFromCache(0x6495ed);
            case "cornsilk":
                return getColorFromCache(0xfff8dc);
            case "crimson":
                return getColorFromCache(0xdc143c);
            case "cyan":
                return getColorFromCache(0x00ffff);
            case "darkblue":
                return getColorFromCache(0x00008b);
            case "darkcyan":
                return getColorFromCache(0x008b8b);
            case "darkgoldenrod":
                return getColorFromCache(0xb8860b);
            case "darkgray":
                return getColorFromCache(0xa9a9a9);
            case "darkgrey":
                return getColorFromCache(0xa9a9a9);
            case "darkgreen":
                return getColorFromCache(0x006400);
            case "darkkhaki":
                return getColorFromCache(0xbdb76b);
            case "darkmagenta":
                return getColorFromCache(0x8b008b);
            case "darkolivegreen":
                return getColorFromCache(0x556b2f);
            case "darkorange":
                return getColorFromCache(0xff8c00);
            case "darkorchid":
                return getColorFromCache(0x9932cc);
            case "darkred":
                return getColorFromCache(0x8b0000);
            case "darksalmon":
                return getColorFromCache(0xe9967a);
            case "darkseagreen":
                return getColorFromCache(0x8fbc8f);
            case "darkslateblue":
                return getColorFromCache(0x483d8b);
            case "darkslategray":
                return getColorFromCache(0x2f4f4f);
            case "darkslategrey":
                return getColorFromCache(0x2f4f4f);
            case "darkturquoise":
                return getColorFromCache(0x00ced1);
            case "darkviolet":
                return getColorFromCache(0x9400d3);
            case "deeppink":
                return getColorFromCache(0xff1493);
            case "deepskyblue":
                return getColorFromCache(0x00bfff);
            case "dimgray":
                return getColorFromCache(0x696969);
            case "dimgrey":
                return getColorFromCache(0x696969);
            case "dodgerblue":
                return getColorFromCache(0x1e90ff);
            case "firebrick":
                return getColorFromCache(0xb22222);
            case "floralwhite":
                return getColorFromCache(0xfffaf0);
            case "forestgreen":
                return getColorFromCache(0x228b22);
            case "fuchsia":
                return getColorFromCache(0xff00ff);
            case "gainsboro":
                return getColorFromCache(0xdcdcdc);
            case "ghostwhite":
                return getColorFromCache(0xf8f8ff);
            case "gold":
                return getColorFromCache(0xffd700);
            case "goldenrod":
                return getColorFromCache(0xdaa520);
            case "gray":
                return getColorFromCache(0x808080);
            case "grey":
                return getColorFromCache(0x808080);
            case "green":
                return getColorFromCache(0x008000);
            case "greenyellow":
                return getColorFromCache(0xadff2f);
            case "honeydew":
                return getColorFromCache(0xf0fff0);
            case "hotpink":
                return getColorFromCache(0xff69b4);
            case "indianred":
                return getColorFromCache(0xcd5c5c);
            case "indigo":
                return getColorFromCache(0x4b0082);
            case "ivory":
                return getColorFromCache(0xfffff0);
            case "khaki":
                return getColorFromCache(0xf0e68c);
            case "lavender":
                return getColorFromCache(0xe6e6fa);
            case "lavenderblush":
                return getColorFromCache(0xfff0f5);
            case "lawngreen":
                return getColorFromCache(0x7cfc00);
            case "lemonchiffon":
                return getColorFromCache(0xfffacd);
            case "lightblue":
                return getColorFromCache(0xadd8e6);
            case "lightcoral":
                return getColorFromCache(0xf08080);
            case "lightcyan":
                return getColorFromCache(0xe0ffff);
            case "lightgoldenrodyellow":
                return getColorFromCache(0xfafad2);
            case "lightgray":
                return getColorFromCache(0xd3d3d3);
            case "lightgrey":
                return getColorFromCache(0xd3d3d3);
            case "lightgreen":
                return getColorFromCache(0x90ee90);
            case "lightpink":
                return getColorFromCache(0xffb6c1);
            case "lightsalmon":
                return getColorFromCache(0xffa07a);
            case "lightseagreen":
                return getColorFromCache(0x20b2aa);
            case "lightskyblue":
                return getColorFromCache(0x87cefa);
            case "lightslategray":
                return getColorFromCache(0x778899);
            case "lightslategrey":
                return getColorFromCache(0x778899);
            case "lightsteelblue":
                return getColorFromCache(0xb0c4de);
            case "lightyellow":
                return getColorFromCache(0xffffe0);
            case "lime":
                return getColorFromCache(0x00ff00);
            case "limegreen":
                return getColorFromCache(0x32cd32);
            case "linen":
                return getColorFromCache(0xfaf0e6);
            case "magenta":
                return getColorFromCache(0xff00ff);
            case "maroon":
                return getColorFromCache(0x800000);
            case "mediumaquamarine":
                return getColorFromCache(0x66cdaa);
            case "mediumblue":
                return getColorFromCache(0x0000cd);
            case "mediumorchid":
                return getColorFromCache(0xba55d3);
            case "mediumpurple":
                return getColorFromCache(0x9370d8);
            case "mediumseagreen":
                return getColorFromCache(0x3cb371);
            case "mediumslateblue":
                return getColorFromCache(0x7b68ee);
            case "mediumspringgreen":
                return getColorFromCache(0x00fa9a);
            case "mediumturquoise":
                return getColorFromCache(0x48d1cc);
            case "mediumvioletred":
                return getColorFromCache(0xc71585);
            case "midnightblue":
                return getColorFromCache(0x191970);
            case "mintcream":
                return getColorFromCache(0xf5fffa);
            case "mistyrose":
                return getColorFromCache(0xffe4e1);
            case "moccasin":
                return getColorFromCache(0xffe4b5);
            case "navajowhite":
                return getColorFromCache(0xffdead);
            case "navy":
                return getColorFromCache(0x000080);
            case "oldlace":
                return getColorFromCache(0xfdf5e6);
            case "olive":
                return getColorFromCache(0x808000);
            case "olivedrab":
                return getColorFromCache(0x6b8e23);
            case "orange":
                return getColorFromCache(0xffa500);
            case "orangered":
                return getColorFromCache(0xff4500);
            case "orchid":
                return getColorFromCache(0xda70d6);
            case "palegoldenrod":
                return getColorFromCache(0xeee8aa);
            case "palegreen":
                return getColorFromCache(0x98fb98);
            case "paleturquoise":
                return getColorFromCache(0xafeeee);
            case "palevioletred":
                return getColorFromCache(0xd87093);
            case "papayawhip":
                return getColorFromCache(0xffefd5);
            case "peachpuff":
                return getColorFromCache(0xffdab9);
            case "peru":
                return getColorFromCache(0xcd853f);
            case "pink":
                return getColorFromCache(0xffc0cb);
            case "plum":
                return getColorFromCache(0xdda0dd);
            case "powderblue":
                return getColorFromCache(0xb0e0e6);
            case "purple":
                return getColorFromCache(0x800080);
            case "red":
                return getColorFromCache(0xff0000);
            case "rosybrown":
                return getColorFromCache(0xbc8f8f);
            case "royalblue":
                return getColorFromCache(0x4169e1);
            case "saddlebrown":
                return getColorFromCache(0x8b4513);
            case "salmon":
                return getColorFromCache(0xfa8072);
            case "sandybrown":
                return getColorFromCache(0xf4a460);
            case "seagreen":
                return getColorFromCache(0x2e8b57);
            case "seashell":
                return getColorFromCache(0xfff5ee);
            case "sienna":
                return getColorFromCache(0xa0522d);
            case "silver":
                return getColorFromCache(0xc0c0c0);
            case "skyblue":
                return getColorFromCache(0x87ceeb);
            case "slateblue":
                return getColorFromCache(0x6a5acd);
            case "slategray":
                return getColorFromCache(0x708090);
            case "slategrey":
                return getColorFromCache(0x708090);
            case "snow":
                return getColorFromCache(0xfffafa);
            case "springgreen":
                return getColorFromCache(0x00ff7f);
            case "steelblue":
                return getColorFromCache(0x4682b4);
            case "tan":
                return getColorFromCache(0xd2b48c);
            case "teal":
                return getColorFromCache(0x008080);
            case "thistle":
                return getColorFromCache(0xd8bfd8);
            case "tomato":
                return getColorFromCache(0xff6347);
            case "turquoise":
                return getColorFromCache(0x40e0d0);
            case "violet":
                return getColorFromCache(0xee82ee);
            case "wheat":
                return getColorFromCache(0xf5deb3);
            case "white":
                return getColorFromCache(0xffffff);
            case "whitesmoke":
                return getColorFromCache(0xf5f5f5);
            case "yellow":
                return getColorFromCache(0xffff00);
            case "yellowgreen":
                return getColorFromCache(0x9acd32);
        }
        
        // if the color is a hex code, try to get the RGB value
        if (label.startsWith("#")) {
            label = label.substring(1);
        }
        try {
            long val = Long.valueOf(label);
            return new Color( (int) val, label.length() > 6);
        } catch (Exception ex) {
            //return defaultColor;
        }

        return defaultColor;

    }

    /**
     * returns the HTML-defined name for the given color. If the color is not
     * defined in the HTML table, then it returns a hex string (# followed by 6
     * hex characters) representing the R,G, B values of the color. (Note that
     * in HTML, "Aqua" = "Cyan" and "Fuchsia" = "Magenta". Cyan and Magenta are
     * returned for these cases.)
     *
     * @param color color of interest
     * @return an HTML color name or hex string
     */
    public static String getLabel(Color color) {
        switch (color.getRGB()) {
            case 0xF0F8FF:
                return "AliceBlue";
            case 0xFAEBD7:
                return "AntiqueWhite";
            //case 0x00FFFF:   // same as cyan.  return cyan instead.
            //    return "Aqua";
            case 0x7FFFD4:
                return "Aquamarine";
            case 0xF0FFFF:
                return "Azure";
            case 0xF5F5DC:
                return "Beige";
            case 0xFFE4C4:
                return "Bisque";
            case 0x000000:
                return "Black";
            case 0xFFEBCD:
                return "BlanchedAlmond";
            case 0x0000FF:
                return "Blue";
            case 0x8A2BE2:
                return "BlueViolet";
            case 0xA52A2A:
                return "Brown";
            case 0xDEB887:
                return "BurlyWood";
            case 0x5F9EA0:
                return "CadetBlue";
            case 0x7FFF00:
                return "Chartreuse";
            case 0xD2691E:
                return "Chocolate";
            case 0xFF7F50:
                return "Coral";
            case 0x6495ED:
                return "CornflowerBlue";
            case 0xFFF8DC:
                return "Cornsilk";
            case 0xDC143C:
                return "Crimson";
            case 0x00FFFF:
                return "Cyan";
            case 0x00008B:
                return "DarkBlue";
            case 0x008B8B:
                return "DarkCyan";
            case 0xB8860B:
                return "DarkGoldenRod";
            case 0xA9A9A9:
                return "DarkGray";
            case 0x006400:
                return "DarkGreen";
            case 0xBDB76B:
                return "DarkKhaki";
            case 0x8B008B:
                return "DarkMagenta";
            case 0x556B2F:
                return "DarkOliveGreen";
            case 0xFF8C00:
                return "Darkorange";
            case 0x9932CC:
                return "DarkOrchid";
            case 0x8B0000:
                return "DarkRed";
            case 0xE9967A:
                return "DarkSalmon";
            case 0x8FBC8F:
                return "DarkSeaGreen";
            case 0x483D8B:
                return "DarkSlateBlue";
            case 0x2F4F4F:
                return "DarkSlateGray";
            case 0x00CED1:
                return "DarkTurquoise";
            case 0x9400D3:
                return "DarkViolet";
            case 0xFF1493:
                return "DeepPink";
            case 0x00BFFF:
                return "DeepSkyBlue";
            case 0x696969:
                return "DimGray";
            case 0x1E90FF:
                return "DodgerBlue";
            case 0xB22222:
                return "FireBrick";
            case 0xFFFAF0:
                return "FloralWhite";
            case 0x228B22:
                return "ForestGreen";
            //case 0xFF00FF:
            //    return "Fuchsia";  // same as magenta.  Return magenta instead.
            case 0xDCDCDC:
                return "Gainsboro";
            case 0xF8F8FF:
                return "GhostWhite";
            case 0xFFD700:
                return "Gold";
            case 0xDAA520:
                return "GoldenRod";
            case 0x808080:
                return "Gray";
            case 0x008000:
                return "Green";
            case 0xADFF2F:
                return "GreenYellow";
            case 0xF0FFF0:
                return "HoneyDew";
            case 0xFF69B4:
                return "HotPink";
            case 0xCD5C5C:
                return "IndianRed ";
            case 0x4B0082:
                return "Indigo ";
            case 0xFFFFF0:
                return "Ivory";
            case 0xF0E68C:
                return "Khaki";
            case 0xE6E6FA:
                return "Lavender";
            case 0xFFF0F5:
                return "LavenderBlush";
            case 0x7CFC00:
                return "LawnGreen";
            case 0xFFFACD:
                return "LemonChiffon";
            case 0xADD8E6:
                return "LightBlue";
            case 0xF08080:
                return "LightCoral";
            case 0xE0FFFF:
                return "LightCyan";
            case 0xFAFAD2:
                return "LightGoldenRodYellow";
            case 0xD3D3D3:
                return "LightGray";
            case 0x90EE90:
                return "LightGreen";
            case 0xFFB6C1:
                return "LightPink";
            case 0xFFA07A:
                return "LightSalmon";
            case 0x20B2AA:
                return "LightSeaGreen";
            case 0x87CEFA:
                return "LightSkyBlue";
            case 0x778899:
                return "LightSlateGray";
            case 0xB0C4DE:
                return "LightSteelBlue";
            case 0xFFFFE0:
                return "LightYellow";
            case 0x00FF00:
                return "Lime";
            case 0x32CD32:
                return "LimeGreen";
            case 0xFAF0E6:
                return "Linen";
            case 0xFF00FF:
                return "Magenta";
            case 0x800000:
                return "Maroon";
            case 0x66CDAA:
                return "MediumAquaMarine";
            case 0x0000CD:
                return "MediumBlue";
            case 0xBA55D3:
                return "MediumOrchid";
            case 0x9370DB:
                return "MediumPurple";
            case 0x3CB371:
                return "MediumSeaGreen";
            case 0x7B68EE:
                return "MediumSlateBlue";
            case 0x00FA9A:
                return "MediumSpringGreen";
            case 0x48D1CC:
                return "MediumTurquoise";
            case 0xC71585:
                return "MediumVioletRed";
            case 0x191970:
                return "MidnightBlue";
            case 0xF5FFFA:
                return "MintCream";
            case 0xFFE4E1:
                return "MistyRose";
            case 0xFFE4B5:
                return "Moccasin";
            case 0xFFDEAD:
                return "NavajoWhite";
            case 0x000080:
                return "Navy";
            case 0xFDF5E6:
                return "OldLace";
            case 0x808000:
                return "Olive";
            case 0x6B8E23:
                return "OliveDrab";
            case 0xFFA500:
                return "Orange";
            case 0xFF4500:
                return "OrangeRed";
            case 0xDA70D6:
                return "Orchid";
            case 0xEEE8AA:
                return "PaleGoldenRod";
            case 0x98FB98:
                return "PaleGreen";
            case 0xAFEEEE:
                return "PaleTurquoise";
            case 0xDB7093:
                return "PaleVioletRed";
            case 0xFFEFD5:
                return "PapayaWhip";
            case 0xFFDAB9:
                return "PeachPuff";
            case 0xCD853F:
                return "Peru";
            case 0xFFC0CB:
                return "Pink";
            case 0xDDA0DD:
                return "Plum";
            case 0xB0E0E6:
                return "PowderBlue";
            case 0x800080:
                return "Purple";
            case 0xFF0000:
                return "Red";
            case 0xBC8F8F:
                return "RosyBrown";
            case 0x4169E1:
                return "RoyalBlue";
            case 0x8B4513:
                return "SaddleBrown";
            case 0xFA8072:
                return "Salmon";
            case 0xF4A460:
                return "SandyBrown";
            case 0x2E8B57:
                return "SeaGreen";
            case 0xFFF5EE:
                return "SeaShell";
            case 0xA0522D:
                return "Sienna";
            case 0xC0C0C0:
                return "Silver";
            case 0x87CEEB:
                return "SkyBlue";
            case 0x6A5ACD:
                return "SlateBlue";
            case 0x708090:
                return "SlateGray";
            case 0xFFFAFA:
                return "Snow";
            case 0x00FF7F:
                return "SpringGreen";
            case 0x4682B4:
                return "SteelBlue";
            case 0xD2B48C:
                return "Tan";
            case 0x008080:
                return "Teal";
            case 0xD8BFD8:
                return "Thistle";
            case 0xFF6347:
                return "Tomato";
            case 0x40E0D0:
                return "Turquoise";
            case 0xEE82EE:
                return "Violet";
            case 0xF5DEB3:
                return "Wheat";
            case 0xFFFFFF:
                return "White";
            case 0xF5F5F5:
                return "WhiteSmoke";
            case 0xFFFF00:
                return "Yellow";
            case 0x9ACD32:
                return "YellowGreen";
            default:
                return "#" + Integer.toHexString(color.getRGB() | 0xFF000000).substring(2);
        }

    }

    private static Color getColorFromCache(int value) {
        Color c = colorCache.get(value);
        if (c == null) {
            c = new Color(value);
            colorCache.put(value, c);
        }
        return c;
    }

    public static void main(String[] args) {
        Color c = Colors.getColor("deepskyblue", Color.yellow);
        System.out.println(c);
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */