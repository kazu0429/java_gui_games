// package jp.fit.jc.it;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;

// BorderLabelView はBorderViewに枠外のラベル付加機能を付けたもの。


public class BorderLabelView extends BorderView {
	String[] north,south,east,west; //上下右左
	String ne,nw,se,sw; //四隅
	public static final int NORTH=1,SOUTH=2,EAST=4,WEST=8;
	public static final int NE=5,NW=9,SE=6,SW=10;
	Font font;
	Color fontColor;

	// 文字のオフセット
	Point northOffset,southOffset,eastOffset,westOffset; //上下右左
	Point neOffset,nwOffset,seOffset,swOffset; //四隅
	Point defaultOffset = new Point(2,2);

	public BorderLabelView(PPanel2 p,int type,Color c) {
		super(p,type,c);
	}

	public void setFont(Font f) { font = f; }
	public Font getFont() { return font;}

	public void setFontColor(Color c) { fontColor = c; }
	public Color getFontColor() { return fontColor; }

	public void setLabelOffset(int ox,int oy,int type) {
		switch(type) {
		case NORTH: northOffset=new Point(ox,oy); break;
		case SOUTH: southOffset=new Point(ox,oy); break;
		case EAST:  eastOffset=new Point(ox,oy); break;
		case WEST:  westOffset=new Point(ox,oy); break;
		case NE: neOffset=new Point(ox,oy); break;
		case NW: nwOffset=new Point(ox,oy); break;
		case SE: seOffset=new Point(ox,oy); break;
		case SW: swOffset=new Point(ox,oy); break;
		default:
			throw new IllegalArgumentException(String.format("NoSuchType:%d ",type ));
		}
	}

	public void setLabels(String[] labels, int type) {
		String[] ls= new String[labels.length];
		for(int i=0;i<labels.length;i++) ls[i] = new String(labels[i]);
		switch(type) {
		case NORTH: north=ls; break;
		case SOUTH: south=ls; break;
		case EAST:  east=ls; break;
		case WEST:  west=ls; break;
		default:
			throw new IllegalArgumentException(String.format("NoSuchType:%d ",type ));
		}
	}

	public String[] getLabels(int type) {
		switch(type) {
		case NORTH: return north;
		case SOUTH: return south;
		case EAST:  return east;
		case WEST:  return west;
		default:
			throw new IllegalArgumentException(String.format("NoSuchType:%d ",type ));
		}
	}

	public void setLabel(String label, int type) {
		switch(type) {
		case NE: ne=new String(label); break;
		case NW: nw=new String(label); break;
		case SE: se=new String(label); break;
		case SW: sw=new String(label); break;
		default:
			throw new IllegalArgumentException(String.format("NoSuchType:%d ",type ));
		}
	}

	public String getLabel(int type) {
		switch(type) {
		case NE: return ne;
		case NW: return nw;
		case SE: return se;
		case SW: return sw;
		default:
			throw new IllegalArgumentException(String.format("NoSuchType:%d ",type ));
		}
	}

	public void setLabel(String label, int pos, int type) {
		switch(type) {
		case NORTH:
			if(north==null) north=new String[panel.num.x];
			north[pos] = new String(label);
			break;
		case SOUTH:
			if(south==null) south=new String[panel.num.x];
			south[pos] = new String(label);
			break;

		case EAST:
			if(east==null) east=new String[panel.num.y];
			east[pos] = new String(label);
			break;
		case WEST:
			if(west==null) west=new String[panel.num.y];
			west[pos] = new String(label);
			break;
		default:
			throw new IllegalArgumentException(String.format("NoSuchType:%d ",type ));
		}
	}

	public String getLabel( int pos, int type) {
		switch(type) {
		case NORTH: return 	north[pos];
		case SOUTH: return south[pos];
		case EAST:  return east[pos];
		case WEST:  return west[pos];
		default:
			throw new IllegalArgumentException(String.format("NoSuchType:%d ",type ));
		}
	}

	@Override
	protected void drawView(Graphics g) {
		super.drawView(g);
		if(font != null) g.setFont(font);
		else g.setFont(panel.getFont());
		if(fontColor != null) g.setColor(fontColor);
		else g.setColor(panel.getForeground());
		int px,py;
		if(north != null) {
			Point off=defaultOffset;
			if(northOffset != null) off=northOffset;
			py = panel.offset.y - off.y;
			for(int x = 0; x < panel.num.x; x++) {
				px = panel.getOriginX(x) + off.x;
				if(north[x] != null) g.drawString(north[x], px, py);
			}
		}

		if(south != null) {
			Point off=defaultOffset;
			if(southOffset != null) off=southOffset;
			py = panel.getOriginY(panel.num.y) + panel.offset.y - off.y;
			for(int x = 0; x < panel.num.x; x++) {
				px = panel.getOriginX(x) + off.x;
				if(south[x] != null) g.drawString(south[x], px, py);
			}
		}

		if(east!= null) {
			Point off=defaultOffset;
			if(eastOffset != null) off=eastOffset;
			px = panel.getOriginX(panel.num.x) + off.x;
			for(int y = 0; y < panel.num.y; y++) {
				py = panel.getOriginY(y+1) - off.y;
				if(east[y] != null) g.drawString(east[y], px, py);
			}
		}

		if(west!= null) {
			Point off=defaultOffset;
			if(westOffset != null) off=westOffset;
			px = 0+off.x;
			for(int y = 0; y < panel.num.y; y++) {
				py = panel.getOriginY(y+1) - off.y;
				if(east[y] != null) g.drawString(east[y], px, py);
			}
		}

		if(ne != null) {
			Point off=defaultOffset;
			if(neOffset != null) off=neOffset;
			px = panel.getOriginX(panel.num.x) + off.x;
			py = panel.offset.y - off.y;
			g.drawString(ne, px, py);
		}

		if(nw != null) {
			Point off=defaultOffset;
			if(nwOffset != null) off=nwOffset;
			px = 0+off.x;
			py = panel.offset.y - off.y;
			g.drawString(nw, px, py);
		}

		if(se != null) {
			Point off=defaultOffset;
			if(seOffset != null) off=seOffset;
			px = panel.getOriginX(panel.num.x) + off.x;
			py = panel.getOriginY(panel.num.y) +
					panel.offset.y - off.y;
			g.drawString(se, px, py);
		}

		if(sw != null) {
			Point off=defaultOffset;
			if(swOffset != null) off=swOffset;
			px = 0+off.x;
			py = panel.getOriginY(panel.num.y) +
					panel.offset.y - off.y;
			g.drawString(ne, px, py);
		}
	}
}
