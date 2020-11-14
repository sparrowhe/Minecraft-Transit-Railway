package mtr.gui;

import mtr.MTR;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Style;
import net.minecraft.util.Identifier;

import java.util.stream.IntStream;

public interface IGui {

	int SQUARE_SIZE = 20;
	int PANEL_WIDTH = 144;
	int TEXT_HEIGHT = 8;
	int TEXT_PADDING = 6;
	int TEXT_FIELD_PADDING = 4;
	int LINE_HEIGHT = 10;

	int RGB_WHITE = 0xFFFFFF;
	int ARGB_WHITE = 0xFFFFFFFF;
	int ARGB_BLACK = 0xFF000000;
	int ARGB_WHITE_TRANSLUCENT = 0x7FFFFFFF;
	int ARGB_BLACK_TRANSLUCENT = 0x7F000000;
	int ARGB_LIGHT_GRAY = 0xFFA8A8A8;
	int ARGB_BACKGROUND = 0xFF121212;

	static String formatStationName(String name) {
		return name.replace('|', ' ');
	}

	static void drawStringWithFont(MatrixStack matrices, TextRenderer textRenderer, String text, int horizontalAlignment, int verticalAlignment, int x, int y, boolean verticalChinese) {
		if (verticalChinese) {
			StringBuilder textBuilder = new StringBuilder();
			for (int i = 0; i < text.length(); i++) {
				boolean isChinese = Character.UnicodeScript.of(text.codePointAt(i)) == Character.UnicodeScript.HAN;
				if (isChinese) {
					textBuilder.append('|');
				}
				textBuilder.append(text, i, i + 1);
				if (isChinese) {
					textBuilder.append('|');
				}
			}
			text = textBuilder.toString();
		}
		while (text.contains("||")) {
			text = text.replace("||", "|");
		}
		final String[] textSplit = text.split("\\|");

		final int[] lineHeights = new int[textSplit.length];
		for (int i = 0; i < textSplit.length; i++) {
			final boolean hasChinese = textSplit[i].codePoints().anyMatch(codePoint -> Character.UnicodeScript.of(codePoint) == Character.UnicodeScript.HAN);
			lineHeights[i] = LINE_HEIGHT * (hasChinese ? 2 : 1);
		}

		final Style style = Style.EMPTY.withFont(new Identifier(MTR.MOD_ID, "mtr"));
		int offset = y - IntStream.of(lineHeights).sum() * verticalAlignment / 2;
		for (int i = 0; i < textSplit.length; i++) {
			final OrderedText orderedText = new LiteralText(textSplit[i]).fillStyle(style).asOrderedText();
			textRenderer.drawWithShadow(matrices, orderedText, x + horizontalAlignment * textRenderer.getWidth(orderedText) / 2F, offset, ARGB_WHITE);
			offset += lineHeights[i];
		}
	}

	static void setPositionAndWidth(AbstractButtonWidget widget, int x, int y, int widgetWidth) {
		widget.x = x;
		widget.y = y;
		widget.setWidth(widgetWidth);
	}

	static int divideColorRGB(int color, int amount) {
		final int r = ((color >> 16) & 0xFF) / amount;
		final int g = ((color >> 8) & 0xFF) / amount;
		final int b = (color & 0xFF) / amount;
		return (r << 16) + (g << 8) + b;
	}

	static void drawRectangle(BufferBuilder buffer, double x1, double y1, double x2, double y2, int color) {
		final float a = (color >> 24 & 0xFF) / 255F;
		final float r = (color >> 16 & 0xFF) / 255F;
		final float g = (color >> 8 & 0xFF) / 255F;
		final float b = (color & 0xFF) / 255F;
		buffer.vertex(x1, y1, 0).color(r, g, b, a).next();
		buffer.vertex(x1, y2, 0).color(r, g, b, a).next();
		buffer.vertex(x2, y2, 0).color(r, g, b, a).next();
		buffer.vertex(x2, y1, 0).color(r, g, b, a).next();
	}
}
