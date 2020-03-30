import java.nio.Buffer;
import java.nio.BufferOverflowException;
import java.io.Writer;
import org.owasp.encoder.Encode;;
import org.ht.template.Parameters;

public class test {
public static void render(Writer writer,Parameters params) {
 try{
 writer.append("<html>\n"); // REGULAR
 writer.append("	<head>\n"); // REGULAR
 writer.append("		<!-- Basic -->\n"); // COMMENT
 writer.append("		<meta charset=\"utf-8\">\n"); // REGULAR
 writer.append("		<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n"); // REGULAR
 writer.append("		<title>\n"); // REGULAR
 writer.append("			Error\n"); // CONTENT
 writer.append("		</title>\n"); // REGULAR
 writer.append("		<!--vsvssfv    -->\n"); // COMMENT
 writer.append("		<!-- Mobile Metas -->\n"); // COMMENT
 writer.append("		<meta ht-if=\"dfsdf > fdfd \" ht-if=\"dfsdf > fdfd \" name=\"viewport\"          content=\"width=device-width, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no\">\n"); // CONTENT
 writer.append("		<meta ht-impoort=\"java.io.Bits\">\n"); // CONTENT
 writer.append("		\n"); // REGULAR
 writer.append("		\n"); // REGULAR
 writer.append("		<!DOCTYPE html>\n"); // REGULAR
 writer.append("		<html lang=\"en\">\n"); // REGULAR
 writer.append("			<head>\n"); // REGULAR
 writer.append("				<meta charset=\"UTF-8\">\n"); // REGULAR
 writer.append("				<title>\n"); // REGULAR
 writer.append("					Title\n"); // CONTENT
 writer.append("				</title>\n"); // REGULAR
 writer.append("			</head>\n"); // REGULAR
 writer.append("			<body>\n"); // REGULAR
 writer.append("				<h1>\n"); // REGULAR
 writer.append("					THIS IS IN TEST 2 FILE\n"); // CONTENT
 writer.append("				</h1>\n"); // REGULAR
 writer.append("			</body>\n"); // REGULAR
 writer.append("		</html>\n"); // REGULAR
 writer.append("		<!-- Web Fonts  -->\n"); // COMMENT
 writer.append("		<!-- Vendor CSS -->\n"); // COMMENT
 writer.append("		<link rel=\"stylesheet\" href=\"/OAS/vendor/bootstrap/css/bootstrap.css\">\n"); // REGULAR
 writer.append("		<link rel=\"stylesheet\"          href=\"/OAS/vendor/font-awesome/css/font-awesome.css\" ht-if=\"dfsdf > fdfd \" ht-text=\"dfjlng > gjfg\">\n"); // CONTENT
 writer.append("		<link rel=\"stylesheet\"          href=\"/OAS/vendor/simple-line-icons/css/simple-line-icons.css\">\n"); // REGULAR
 writer.append("		<link rel=\"stylesheet\" href=\"/OAS/vendor/owl.carousel/assets/owl.carousel.min.css\">\n"); // REGULAR
 writer.append("		<link rel=\"stylesheet\" href=\"/OAS/vendor/owl.carousel/assets/owl.theme.default.min.css\">\n"); // REGULAR
 writer.append("		<link rel=\"stylesheet\" href=\"/OAS/vendor/magnific-popup/magnific-popup.css\">\n"); // REGULAR
 writer.append("		<!-- Theme CSS -->\n"); // COMMENT
 writer.append("		<link rel=\"stylesheet\" href=\"/OAS/css/theme.css\">\n"); // REGULAR
 writer.append("		<link rel=\"stylesheet\" href=\"/OAS/css/theme-elements.css\">\n"); // REGULAR
 writer.append("		<link rel=\"stylesheet\" href=\"/OAS/css/theme-blog.css\">\n"); // REGULAR
 writer.append("		<link rel=\"stylesheet\" href=\"/OAS/css/theme-shop.css\">\n"); // REGULAR
 writer.append("		<link rel=\"stylesheet\" href=\"/OAS/css/theme-animate.css\">\n"); // REGULAR
 writer.append("		<!-- Current Page CSS -->\n"); // COMMENT
 writer.append("		<link rel=\"stylesheet\" href=\"/OAS/vendor/rs-plugin/css/settings.css\" media=\"screen\">\n"); // REGULAR
 writer.append("		<link rel=\"stylesheet\" href=\"/OAS/vendor/rs-plugin/css/layers.css\" media=\"screen\">\n"); // REGULAR
 writer.append("		<link rel=\"stylesheet\" href=\"/OAS/vendor/rs-plugin/css/navigation.css\" media=\"screen\">\n"); // REGULAR
 writer.append("		<link rel=\"stylesheet\" href=\"/OAS/vendor/circle-flip-slideshow/css/component.css\" media=\"screen\">\n"); // REGULAR
 writer.append("		<!-- Skin CSS -->\n"); // COMMENT
 writer.append("		<link rel=\"stylesheet\" href=\"/OAS/css/skins/default.css\">\n"); // REGULAR
 writer.append("		<!-- Theme Custom CSS -->\n"); // COMMENT
 writer.append("		<link rel=\"stylesheet\" href=\"/OAS/css/custom.css\">\n"); // REGULAR
 writer.append("		<!-- Head Libs -->\n"); // COMMENT
 writer.append("		<script src=\"/OAS/vendor/modernizr/modernizr.js\">\n"); // REGULAR
 writer.append("			\n"); // SCRIPT
 writer.append("		</script>\n"); // REGULAR
 writer.append("		<!--[if IE]>\n"); // COMMENT
 writer.append("		<gdfg>fgdgfgd</gdfg>\n"); // COMMENT
 writer.append("		<link rel=\"stylesheet\" href=\"css/ie.css\">\n"); // COMMENT
 writer.append("		<![endif]\n"); // COMMENT
 writer.append("		-->\n"); // COMMENT
 writer.append("		<!--[if lte IE 8]>\n"); // COMMENT
 writer.append("		<script src=\"vendor/respond/respond.js\"></script>\n"); // COMMENT
 writer.append("		<script src=\"vendor/excanvas/excanvas.js\"></script>\n"); // COMMENT
 writer.append("		<![endif]\n"); // COMMENT
 writer.append("		-->\n"); // COMMENT
 writer.append("	</head>\n"); // REGULAR
 writer.append("	<body>\n"); // REGULAR
 writer.append("		<div class=\"body coming-soon\">\n"); // REGULAR
 writer.append("			<div role=\"main\" class=\"main\">\n"); // REGULAR
 writer.append("				<div class=\"container\">\n"); // REGULAR
 writer.append("					<div class=\"row\">\n"); // REGULAR
 writer.append("						<div class=\"col-md-12\">\n"); // REGULAR
 writer.append("							<hr class=\"tall\">\n"); // REGULAR
 writer.append("							mndmndsmsd\n"); // CONTENT
 writer.append("							|dfndsnsdfn |").append(Encode.forHtmlContent(String.valueOf( 4>5 ))).append("|nmdssd|\n"); // CONTENT
 writer.append("							ndsfmndsfmndsfmdfs\n"); // CONTENT
 writer.append("							|dsfndsfmdsfm,|").append(Encode.forHtmlContent(String.valueOf(7 < 10 ))).append(" |vhcgch|\n"); // CONTENT
 writer.append("							<br>\n"); // REGULAR
 writer.append("						</div>\n"); // REGULAR
 writer.append("					</div>\n"); // REGULAR
 writer.append("					<div class=\"row\">\n"); // REGULAR
 writer.append("						<div class=\"col-md-12 center\">\n"); // REGULAR
 writer.append("							<h1 class=\"mb-sm small\">\n"); // REGULAR
 writer.append("								Error\n"); // CONTENT
 writer.append("							</h1>\n"); // REGULAR
 writer.append("							<p class=\"lead\">\n"); // REGULAR
 writer.append("								Oops! an error occurred\n"); // CONTENT
 writer.append("								<br>\n"); // REGULAR
 writer.append("								Contact administrative staff for any problem\n"); // CONTENT
 writer.append("							</p>\n"); // REGULAR
 writer.append("						</div>\n"); // REGULAR
 writer.append("					</div>\n"); // REGULAR
 writer.append("					<div class=\"row\">\n"); // REGULAR
 writer.append("						<div class=\"col-md-12\">\n"); // REGULAR
 writer.append("							<center>\n"); // REGULAR
 writer.append("								<a class=\"\" href=\"/OAS/logout\">\n"); // REGULAR
 writer.append("									Log Out\n"); // CONTENT
 writer.append("								</a>\n"); // REGULAR
 writer.append("							</center>\n"); // REGULAR
 writer.append("						</div>\n"); // REGULAR
 writer.append("					</div>\n"); // REGULAR
 writer.append("					<div class=\"row\">\n"); // REGULAR
 writer.append("						<div class=\"col-md-12\">\n"); // REGULAR
 writer.append("							<hr class=\"tall\">\n"); // REGULAR
 writer.append("						</div>\n"); // REGULAR
 writer.append("					</div>\n"); // REGULAR
 writer.append("				</div>\n"); // REGULAR
 writer.append("			</div>\n"); // REGULAR
 writer.append("		</div>\n"); // REGULAR
 writer.append("		<!-- Vendor -->\n"); // COMMENT
 writer.append("		<!--[if lt IE 9]>\n"); // COMMENT
 writer.append("		<script src=\"//code.jquery.com/jquery-1.11.3.min.js\"></script>\n"); // COMMENT
 writer.append("		<![endif]\n"); // COMMENT
 writer.append("		-->\n"); // COMMENT
 writer.append("		<!--[if gte IE 9]>\n"); // COMMENT
 writer.append("		<!\n"); // COMMENT
 writer.append("		-->\n"); // COMMENT
 writer.append("		<script src=\"/OAS/vendor/jquery/jquery.js\">\n"); // REGULAR
 writer.append("			\n"); // SCRIPT
 writer.append("		</script>\n"); // REGULAR
 writer.append("		<!--<![endif]-->\n"); // COMMENT
 writer.append("		<script src=\"/OAS/vendor/jquery.appear/jquery.appear.js\">\n"); // REGULAR
 writer.append("			\n"); // SCRIPT
 writer.append("		</script>\n"); // REGULAR
 writer.append("		<script src=\"/OAS/vendor/jquery.easing/jquery.easing.js\">\n"); // REGULAR
 writer.append("			\n"); // SCRIPT
 writer.append("		</script>\n"); // REGULAR
 writer.append("		<script src=\"/OAS/vendor/jquery-cookie/jquery-cookie.js\">\n"); // REGULAR
 writer.append("			\n"); // SCRIPT
 writer.append("		</script>\n"); // REGULAR
 writer.append("		<script src=\"/OAS/vendor/bootstrap/js/bootstrap.js\">\n"); // REGULAR
 writer.append("			\n"); // SCRIPT
 writer.append("		</script>\n"); // REGULAR
 writer.append("		<script src=\"/OAS/vendor/common/common.js\">\n"); // REGULAR
 writer.append("			\n"); // SCRIPT
 writer.append("		</script>\n"); // REGULAR
 writer.append("		<script src=\"/OAS/vendor/jquery.validation/jquery.validation.js\">\n"); // REGULAR
 writer.append("			\n"); // SCRIPT
 writer.append("		</script>\n"); // REGULAR
 writer.append("		<script src=\"/OAS/vendor/jquery.stellar/jquery.stellar.js\">\n"); // REGULAR
 writer.append("			\n"); // SCRIPT
 writer.append("		</script>\n"); // REGULAR
 writer.append("		<script src=\"/OAS/vendor/jquery.easy-pie-chart/jquery.easy-pie-chart.js\">\n"); // REGULAR
 writer.append("			\n"); // SCRIPT
 writer.append("		</script>\n"); // REGULAR
 writer.append("		<script src=\"/OAS/vendor/jquery.gmap/jquery.gmap.js\">\n"); // REGULAR
 writer.append("			\n"); // SCRIPT
 writer.append("		</script>\n"); // REGULAR
 writer.append("		<script src=\"/OAS/vendor/jquery.lazyload/jquery.lazyload.js\">\n"); // REGULAR
 writer.append("			\n"); // SCRIPT
 writer.append("		</script>\n"); // REGULAR
 writer.append("		<script src=\"/OAS/vendor/isotope/jquery.isotope.js\">\n"); // REGULAR
 writer.append("			\n"); // SCRIPT
 writer.append("		</script>\n"); // REGULAR
 writer.append("		<script src=\"/OAS/vendor/owl.carousel/owl.carousel.js\">\n"); // REGULAR
 writer.append("			\n"); // SCRIPT
 writer.append("		</script>\n"); // REGULAR
 writer.append("		<script src=\"/OAS/vendor/magnific-popup/jquery.magnific-popup.js\">\n"); // REGULAR
 writer.append("			\n"); // SCRIPT
 writer.append("		</script>\n"); // REGULAR
 writer.append("		<script src=\"/OAS/vendor/vide/vide.js\">\n"); // REGULAR
 writer.append("			\n"); // SCRIPT
 writer.append("		</script>\n"); // REGULAR
 writer.append("		<!-- Theme Base, Components and Settings -->\n"); // COMMENT
 writer.append("		<script src=\"/OAS/js/theme.js\">\n"); // REGULAR
 writer.append("			\n"); // SCRIPT
 writer.append("		</script>\n"); // REGULAR
 writer.append("		<!-- Specific Page Vendor and Views -->\n"); // COMMENT
 writer.append("		<script src=\"/OAS/vendor/rs-plugin/js/jquery.themepunch.tools.min.js\">\n"); // REGULAR
 writer.append("			\n"); // SCRIPT
 writer.append("		</script>\n"); // REGULAR
 writer.append("		<script src=\"/OAS/vendor/rs-plugin/js/jquery.themepunch.revolution.min.js\">\n"); // REGULAR
 writer.append("			\n"); // SCRIPT
 writer.append("		</script>\n"); // REGULAR
 writer.append("		<script src=\"/OAS/vendor/rs-plugin/js/extensions/revolution.extension.actions.min.js\">\n"); // REGULAR
 writer.append("			\n"); // SCRIPT
 writer.append("		</script>\n"); // REGULAR
 writer.append("		<script src=\"/OAS/vendor/rs-plugin/js/extensions/revolution.extension.carousel.min.js\">\n"); // REGULAR
 writer.append("			\n"); // SCRIPT
 writer.append("		</script>\n"); // REGULAR
 writer.append("		<script src=\"/OAS/vendor/rs-plugin/js/extensions/revolution.extension.kenburn.min.js\">\n"); // REGULAR
 writer.append("			\n"); // SCRIPT
 writer.append("		</script>\n"); // REGULAR
 writer.append("		<script src=\"/OAS/vendor/rs-plugin/js/extensions/revolution.extension.layeranimation.min.js\">\n"); // REGULAR
 writer.append("			\n"); // SCRIPT
 writer.append("		</script>\n"); // REGULAR
 writer.append("		<script src=\"/OAS/vendor/rs-plugin/js/extensions/revolution.extension.migration.min.js\">\n"); // REGULAR
 writer.append("			\n"); // SCRIPT
 writer.append("		</script>\n"); // REGULAR
 writer.append("		<script src=\"/OAS/vendor/rs-plugin/js/extensions/revolution.extension.navigation.min.js\">\n"); // REGULAR
 writer.append("			\n"); // SCRIPT
 writer.append("		</script>\n"); // REGULAR
 writer.append("		<script src=\"/OAS/vendor/rs-plugin/js/extensions/revolution.extension.parallax.min.js\">\n"); // REGULAR
 writer.append("			\n"); // SCRIPT
 writer.append("		</script>\n"); // REGULAR
 writer.append("		<script src=\"/OAS/vendor/rs-plugin/js/extensions/revolution.extension.slideanims.min.js\">\n"); // REGULAR
 writer.append("			\n"); // SCRIPT
 writer.append("		</script>\n"); // REGULAR
 writer.append("		<script src=\"/OAS/vendor/rs-plugin/js/extensions/revolution.extension.video.min.js\">\n"); // REGULAR
 writer.append("			\n"); // SCRIPT
 writer.append("		</script>\n"); // REGULAR
 writer.append("		<script src=\"/OAS/vendor/circle-flip-slideshow/js/jquery.flipshow.js\">\n"); // REGULAR
 writer.append("			\n"); // SCRIPT
 writer.append("		</script>\n"); // REGULAR
 writer.append("		<script src=\"/OAS/js/views/view.home.js\">\n"); // REGULAR
 writer.append("			\n"); // SCRIPT
 writer.append("		</script>\n"); // REGULAR
 writer.append("		<!-- Theme Custom -->\n"); // COMMENT
 writer.append("		<script src=\"/OAS/js/custom.js\">\n"); // REGULAR
 writer.append("			\n"); // SCRIPT
 writer.append("		</script>\n"); // REGULAR
 writer.append("		<!-- Theme Initialization Files -->\n"); // COMMENT
 writer.append("		<script src=\"/OAS/js/theme.init.js\">\n"); // REGULAR
 writer.append("			\n"); // SCRIPT
 writer.append("		</script>\n"); // REGULAR
 writer.append("		<!-- Google Analytics: Change UA-XXXXX-X to be your site\'s ID. Go to http://www.google.com/analytics/ for more information.<script type=\"text/javascript\">\n"); // COMMENT
 writer.append("		var _gaq = _gaq || [];\n"); // COMMENT
 writer.append("		_gaq.push([\'_setAccount\', \'UA-12345678-1\']);\n"); // COMMENT
 writer.append("		_gaq.push([\'_trackPageview\']);\n"); // COMMENT
 writer.append("		(function() {\n"); // COMMENT
 writer.append("		var ga = document.createElement(\'script\'); ga.type = \'text/javascript\'; ga.async = true;\n"); // COMMENT
 writer.append("		ga.src = (\'https:\' == document.location.protocol ? \'https://ssl\' : \'http://www\') + \'.google-analytics.com/ga.js\';\n"); // COMMENT
 writer.append("		var s = document.getElementsByTagName(\'script\')[0]; s.parentNode.insertBefore(ga, s);\n"); // COMMENT
 writer.append("		})();\n"); // COMMENT
 writer.append("		</script>\n"); // COMMENT
 writer.append("		-->\n"); // COMMENT
 writer.append("	</body>\n"); // REGULAR
 writer.append("</html>\n"); // REGULAR
 }catch(Exception e){
  throw new RuntimeException(e);
 }
 }
}