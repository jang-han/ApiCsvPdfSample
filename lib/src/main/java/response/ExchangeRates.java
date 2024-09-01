package response;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

@XmlRootElement(name = "Envelope", namespace = "http://www.gesmes.org/xml/2002-08-01")
@XmlType(propOrder = {"cube"})
public class ExchangeRates {

    private CubeData cube;

    @XmlElement(name = "Cube", namespace = "http://www.ecb.int/vocabulary/2002-08-01/eurofxref")
    public CubeData getCube() {
        return cube;
    }

    public void setCube(CubeData cube) {
        this.cube = cube;
    }

    public static class CubeData {
        private List<TimeCube> cubes;

        @XmlElement(name = "Cube", namespace = "http://www.ecb.int/vocabulary/2002-08-01/eurofxref")
        public List<TimeCube> getCubes() {
            return cubes;
        }

        public void setCubes(List<TimeCube> cubes) {
            this.cubes = cubes;
        }
    }

    public static class TimeCube {
        private String time;
        private List<Cube> rates;

        @XmlAttribute(name = "time")
        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        @XmlElement(name = "Cube", namespace = "http://www.ecb.int/vocabulary/2002-08-01/eurofxref")
        public List<Cube> getRates() {
            return rates;
        }

        public void setRates(List<Cube> rates) {
            this.rates = rates;
        }
    }

    public static class Cube {
        private String currency;
        private String rate;

        @XmlAttribute(name = "currency")
        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        @XmlAttribute(name = "rate")
        public String getRate() {
            return rate;
        }

        public void setRate(String rate) {
            this.rate = rate;
        }
    }
}
