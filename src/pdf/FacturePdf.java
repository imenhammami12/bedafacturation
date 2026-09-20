package pdf;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import entities.Facture;

/**
 * Génère une facture PDF (A4) au design professionnel.
 * Utilisation : FacturePdf.generate(facture, new File("facture_12.pdf"));
 */
public class FacturePdf {

    // ====== A MODIFIER : informations de votre société ======
    private static final String SOCIETE   = "BEDA FACTURATION";
    private static final String ADRESSE   = "Avenue Habib Bourguiba, Tunis";
    private static final String TELEPHONE = "Tel : +216 71 000 000";
    private static final String EMAIL     = "contact@bedafacturation.tn";
    private static final String MATRICULE = "M.F : 1234567/A/M/000";
    // =========================================================

    // Couleurs (R, G, B)
    private static final int[] BLEU_FONCE = {31, 58, 95};
    private static final int[] BLEU_CLAIR = {234, 240, 249};
    private static final int[] GRIS_TEXTE = {90, 98, 110};
    private static final int[] GRIS_LIGNE = {208, 214, 222};
    private static final int[] NOIR       = {33, 37, 41};
    private static final int[] BLANC      = {255, 255, 255};

    private static final PDFont NORMAL = PDType1Font.HELVETICA;
    private static final PDFont GRAS   = PDType1Font.HELVETICA_BOLD;

    private static final float PAGE_W = PDRectangle.A4.getWidth();   // 595
    private static final float PAGE_H = PDRectangle.A4.getHeight();  // 842
    private static final float MARGE  = 50;

    private static final DecimalFormat MONTANT;
    static {
        DecimalFormatSymbols s = new DecimalFormatSymbols();
        s.setGroupingSeparator(' ');
        s.setDecimalSeparator(',');
        MONTANT = new DecimalFormat("#,##0.000", s);
    }

    public static void generate(Facture f, File out) throws IOException {
        try (PDDocument doc = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            doc.addPage(page);

            try (PDPageContentStream cs = new PDPageContentStream(doc, page)) {
                dessinerEntete(doc, cs, f);
                float y = dessinerInfos(cs, f);
                y = dessinerTableau(cs, f, y);
                y = dessinerTotaux(cs, f, y);
                dessinerPied(cs);
            }
            doc.save(out);
        }
    }

    // ------------------------------------------------------------------
    // EN-TETE (bandeau bleu, logo, société, titre FACTURE)
    // ------------------------------------------------------------------
    private static void dessinerEntete(PDDocument doc, PDPageContentStream cs, Facture f) throws IOException {
        float hauteur = 120;
        rectPlein(cs, 0, PAGE_H - hauteur, PAGE_W, hauteur, BLEU_FONCE);

        // Logo (facultatif : ignoré s'il est introuvable)
        float texteX = MARGE;
        try (InputStream in = FacturePdf.class.getResourceAsStream("/resources/logo.png")) {
            if (in != null) {
                BufferedImage image = ImageIO.read(in);
                PDImageXObject logo = LosslessFactory.createFromImage(doc, image);
                rectPlein(cs, MARGE, PAGE_H - 92, 56, 56, BLANC);
                cs.drawImage(logo, MARGE + 4, PAGE_H - 88, 48, 48);
                texteX = MARGE + 70;
            }
        } catch (Exception e) {
            // pas de logo : on continue sans
        }

        texte(cs, GRAS, 16, BLANC, texteX, PAGE_H - 52, SOCIETE);
        texte(cs, NORMAL, 9, BLANC, texteX, PAGE_H - 68, ADRESSE);
        texte(cs, NORMAL, 9, BLANC, texteX, PAGE_H - 80, TELEPHONE + "   |   " + EMAIL);

        // Titre à droite
        texteDroite(cs, GRAS, 30, BLANC, PAGE_W - MARGE, PAGE_H - 58, "FACTURE");
        texteDroite(cs, NORMAL, 10, BLANC, PAGE_W - MARGE, PAGE_H - 78,
                "N° " + String.format("%06d", f.getId()));
    }

    // ------------------------------------------------------------------
    // BLOCS D'INFORMATIONS (client / détails)
    // ------------------------------------------------------------------
    private static float dessinerInfos(PDPageContentStream cs, Facture f) throws IOException {
        float top = PAGE_H - 120 - 30;
        float largeur = (PAGE_W - 2 * MARGE - 20) / 2;
        float haut = 78;

        // Bloc gauche : client
        rectPlein(cs, MARGE, top - haut, largeur, haut, BLEU_CLAIR);
        rectPlein(cs, MARGE, top - haut, 4, haut, BLEU_FONCE);
        texte(cs, GRAS, 9, BLEU_FONCE, MARGE + 16, top - 18, "FACTURE A");
        texte(cs, GRAS, 13, NOIR, MARGE + 16, top - 40, "Client N° " + f.getCodeClient());
        texte(cs, NORMAL, 9, GRIS_TEXTE, MARGE + 16, top - 58, "Code client : " + f.getCodeClient());

        // Bloc droit : détails
        float x2 = MARGE + largeur + 20;
        rectPlein(cs, x2, top - haut, largeur, haut, BLEU_CLAIR);
        rectPlein(cs, x2, top - haut, 4, haut, BLEU_FONCE);
        texte(cs, GRAS, 9, BLEU_FONCE, x2 + 16, top - 18, "DETAILS");
        texte(cs, NORMAL, 10, GRIS_TEXTE, x2 + 16, top - 38, "Date d'émission");
        texteDroite(cs, GRAS, 10, NOIR, x2 + largeur - 14, top - 38, formaterDate(f.getDate()));
        texte(cs, NORMAL, 10, GRIS_TEXTE, x2 + 16, top - 56, "Référence");
        texteDroite(cs, GRAS, 10, NOIR, x2 + largeur - 14, top - 56, "FAC-" + String.format("%06d", f.getId()));

        return top - haut - 30;
    }

    // ------------------------------------------------------------------
    // TABLEAU DES ARTICLES
    // ------------------------------------------------------------------
    private static float dessinerTableau(PDPageContentStream cs, Facture f, float y) throws IOException {
        float[] cols = {235, 70, 95, 95};              // total = 495
        float x0 = MARGE;
        float tailleTexte = 10;
        float hauteurEntete = 26;

        // En-tête du tableau
        rectPlein(cs, x0, y - hauteurEntete, 495, hauteurEntete, BLEU_FONCE);
        float x = x0;
        String[] titres = {"Désignation", "Quantité", "Prix unitaire", "Montant HT"};
        for (int i = 0; i < titres.length; i++) {
            if (i == 0) {
                texte(cs, GRAS, 10, BLANC, x + 10, y - 17, titres[i]);
            } else {
                texteDroite(cs, GRAS, 10, BLANC, x + cols[i] - 10, y - 17, titres[i]);
            }
            x += cols[i];
        }
        y -= hauteurEntete;

        // Ligne de données (la désignation peut passer sur plusieurs lignes)
        List<String> lignes = couper(f.getDesignation(), NORMAL, tailleTexte, cols[0] - 20);
        float hauteurLigne = Math.max(30, 16 + lignes.size() * 13);
        rectPlein(cs, x0, y - hauteurLigne, 495, hauteurLigne, BLANC);
        cs.setStrokingColor(GRIS_LIGNE[0], GRIS_LIGNE[1], GRIS_LIGNE[2]);
        cs.setLineWidth(0.8f);
        cs.addRect(x0, y - hauteurLigne, 495, hauteurLigne);
        cs.stroke();

        float ty = y - 19;
        for (String l : lignes) {
            texte(cs, NORMAL, tailleTexte, NOIR, x0 + 10, ty, l);
            ty -= 13;
        }
        float cx = x0 + cols[0];
        texteDroite(cs, NORMAL, tailleTexte, NOIR, cx + cols[1] - 10, y - 19, String.valueOf(f.getQuantite()));
        cx += cols[1];
        texteDroite(cs, NORMAL, tailleTexte, NOIR, cx + cols[2] - 10, y - 19, montant(f.getPU()));
        cx += cols[2];
        texteDroite(cs, NORMAL, tailleTexte, NOIR, cx + cols[3] - 10, y - 19, montant(f.getPt()));

        return y - hauteurLigne - 30;
    }

    // ------------------------------------------------------------------
    // TOTAUX (à droite) + NET A PAYER
    // ------------------------------------------------------------------
    private static float dessinerTotaux(PDPageContentStream cs, Facture f, float y) throws IOException {
        float largeur = 240;
        float x = PAGE_W - MARGE - largeur;
        float hLigne = 24;

        float timbre = f.getNetAPayer() - f.getTotalTTC();

        String[][] lignes = {
                {"Total HT",              montant(f.getTotal())},
                {"FODEC (1 %)",           montant(f.getFodec())},
                {"T.V.A (19 %)",          montant(f.getTVA())},
                {"Total TTC",             montant(f.getTotalTTC())},
                {"Timbre fiscal",         montant(timbre)}
        };

        for (int i = 0; i < lignes.length; i++) {
            if (i % 2 == 0) {
                rectPlein(cs, x, y - hLigne, largeur, hLigne, BLEU_CLAIR);
            }
            PDFont police = (i == 3) ? GRAS : NORMAL;
            texte(cs, police, 10, GRIS_TEXTE, x + 12, y - 16, lignes[i][0]);
            texteDroite(cs, police, 10, NOIR, x + largeur - 12, y - 16, lignes[i][1]);
            y -= hLigne;
        }

        // Bandeau NET A PAYER
        float hNet = 38;
        rectPlein(cs, x, y - hNet, largeur, hNet, BLEU_FONCE);
        texte(cs, GRAS, 11, BLANC, x + 12, y - 24, "NET A PAYER");
        texteDroite(cs, GRAS, 13, BLANC, x + largeur - 12, y - 24, montant(f.getNetAPayer()));

        return y - hNet - 30;
    }

    // ------------------------------------------------------------------
    // PIED DE PAGE
    // ------------------------------------------------------------------
    private static void dessinerPied(PDPageContentStream cs) throws IOException {
        // Cadre signature
        float boxW = 170, boxH = 70, boxY = 130;
        cs.setStrokingColor(GRIS_LIGNE[0], GRIS_LIGNE[1], GRIS_LIGNE[2]);
        cs.setLineWidth(0.8f);
        cs.addRect(MARGE, boxY, boxW, boxH);
        cs.stroke();
        texte(cs, GRAS, 9, GRIS_TEXTE, MARGE + 10, boxY + boxH - 16, "Signature et cachet");

        // Ligne de séparation
        cs.setStrokingColor(BLEU_FONCE[0], BLEU_FONCE[1], BLEU_FONCE[2]);
        cs.setLineWidth(1.5f);
        cs.moveTo(MARGE, 90);
        cs.lineTo(PAGE_W - MARGE, 90);
        cs.stroke();

        texteCentre(cs, GRAS, 11, BLEU_FONCE, PAGE_W / 2, 70, "Merci pour votre confiance !");
        texteCentre(cs, NORMAL, 8, GRIS_TEXTE, PAGE_W / 2, 55,
                SOCIETE + "  -  " + MATRICULE + "  -  " + ADRESSE);
        texteCentre(cs, NORMAL, 8, GRIS_TEXTE, PAGE_W / 2, 43,
                "Montants exprimés en Dinar Tunisien (DT)  -  Généré le "
                        + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()));
    }

    // ==================================================================
    // OUTILS DE DESSIN
    // ==================================================================
    private static void rectPlein(PDPageContentStream cs, float x, float y, float w, float h, int[] c) throws IOException {
        cs.setNonStrokingColor(c[0], c[1], c[2]);
        cs.addRect(x, y, w, h);
        cs.fill();
    }

    private static void texte(PDPageContentStream cs, PDFont police, float taille, int[] c,
                              float x, float y, String s) throws IOException {
        cs.beginText();
        cs.setFont(police, taille);
        cs.setNonStrokingColor(c[0], c[1], c[2]);
        cs.newLineAtOffset(x, y);
        cs.showText(nettoyer(s));
        cs.endText();
    }

    private static void texteDroite(PDPageContentStream cs, PDFont police, float taille, int[] c,
                                    float xDroite, float y, String s) throws IOException {
        float w = largeur(police, taille, s);
        texte(cs, police, taille, c, xDroite - w, y, s);
    }

    private static void texteCentre(PDPageContentStream cs, PDFont police, float taille, int[] c,
                                    float xCentre, float y, String s) throws IOException {
        float w = largeur(police, taille, s);
        texte(cs, police, taille, c, xCentre - w / 2, y, s);
    }

    private static float largeur(PDFont police, float taille, String s) throws IOException {
        return police.getStringWidth(nettoyer(s)) / 1000f * taille;
    }

    /** Coupe un texte en plusieurs lignes pour qu'il tienne dans la largeur donnée. */
    private static List<String> couper(String texte, PDFont police, float taille, float largeurMax) throws IOException {
        List<String> lignes = new ArrayList<String>();
        String t = nettoyer(texte == null ? "" : texte);
        StringBuilder courante = new StringBuilder();
        for (String mot : t.split(" ")) {
            String essai = courante.length() == 0 ? mot : courante + " " + mot;
            if (largeur(police, taille, essai) <= largeurMax) {
                courante = new StringBuilder(essai);
            } else {
                if (courante.length() > 0) {
                    lignes.add(courante.toString());
                }
                courante = new StringBuilder(mot);
            }
        }
        if (courante.length() > 0 || lignes.isEmpty()) {
            lignes.add(courante.toString());
        }
        return lignes;
    }

    private static String montant(float valeur) {
        return MONTANT.format(valeur) + " DT";
    }

    private static String formaterDate(java.util.Date d) {
        return d == null ? "-" : new SimpleDateFormat("dd/MM/yyyy").format(d);
    }

    /** Remplace les caractères que la police PDF standard ne sait pas afficher. */
    private static String nettoyer(String s) {
        if (s == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (char c : s.toCharArray()) {
            if (c == '\n' || c == '\r' || c == '\t') {
                sb.append(' ');
            } else if (c < 32 || c > 255) {
                sb.append('?');
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}