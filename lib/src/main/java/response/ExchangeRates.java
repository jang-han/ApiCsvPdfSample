package response;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Envelope", namespace = "http://www.gesmes.org/xml/2002-08-01")
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

        @XmlElement(name = "Cube", namespace = "http://www.ecb.int/vocabulary/2002-08-01/eurofxref")
        public List<Cube> getRates() {
            return rates;
        }

        public void setRates(List<Cube> rates) {
            this.rates = rates;
        }

        @XmlElement(name = "time", namespace = "http://www.ecb.int/vocabulary/2002-08-01/eurofxref")
        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }

    public static class Cube {
        private String currency;
        private String rate;

        @XmlElement(name = "currency", namespace = "http://www.ecb.int/vocabulary/2002-08-01/eurofxref")
        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        @XmlElement(name = "rate", namespace = "http://www.ecb.int/vocabulary/2002-08-01/eurofxref")
        public String getRate() {
            return rate;
        }

        public void setRate(String rate) {
            this.rate = rate;
        }
    }
}