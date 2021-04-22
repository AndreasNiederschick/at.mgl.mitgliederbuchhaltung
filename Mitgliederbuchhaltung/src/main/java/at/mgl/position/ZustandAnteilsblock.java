package at.mgl.position;

import at.mgl.transaktion.MglTransaktionsTyp;

public enum ZustandAnteilsblock {
	
	Start {
		@Override
		public ZustandAnteilsblock naechsterStatus (MglTransaktionsTyp mglTransaktionTyp, boolean istStorno) {
			switch(mglTransaktionTyp) {
				case Zeichnung: return Gezeichnet;
				default: return Start;
			}
		}
	},
	Gezeichnet {
		@Override
		public ZustandAnteilsblock naechsterStatus (MglTransaktionsTyp mglTransaktionTyp, boolean istStorno) {
			switch(mglTransaktionTyp) {
			case Zeichnung: if (istStorno) {return Start;} else {return Gezeichnet;}
			case Kuendigung: if (istStorno) {return Gezeichnet;} else {return Gekuendigt;}
			default: return Gezeichnet;
			}
		}
	},
	Gekuendigt {
		@Override
		public ZustandAnteilsblock naechsterStatus (MglTransaktionsTyp mglTransaktionTyp, boolean istStorno) {
			switch(mglTransaktionTyp) {
			case Kuendigung: if (istStorno) {return Gezeichnet;} else {return Gekuendigt;}
			case Auszahlungssperrfrist: if (istStorno) {return Gekuendigt;} else {return Auszahlungssperrfrist;}
			default: return Gekuendigt;
			}
		}
	},
	Auszahlungssperrfrist {
		@Override
		public ZustandAnteilsblock naechsterStatus (MglTransaktionsTyp mglTransaktionTyp, boolean istStorno) {
			switch(mglTransaktionTyp) {
			case Auszahlungssperrfrist: if (istStorno) {return Gekuendigt;} else {return Auszahlungssperrfrist;}
			case Auszahlung: if (istStorno) {return Auszahlungssperrfrist;} else {return Ausgezahlt;}
			default: return Auszahlungssperrfrist;
			}
		}
	},
	Ausgezahlt {
		@Override
		public ZustandAnteilsblock naechsterStatus (MglTransaktionsTyp mglTransaktionTyp, boolean istStorno) {
			switch(mglTransaktionTyp) {
			case Auszahlung: if (istStorno) {return Auszahlungssperrfrist;} else {return Ausgezahlt;}
			default: return Ausgezahlt;
			}
		}
	};
	
	public abstract ZustandAnteilsblock naechsterStatus (MglTransaktionsTyp mglTransaktionTyp, boolean istStorno);

}
