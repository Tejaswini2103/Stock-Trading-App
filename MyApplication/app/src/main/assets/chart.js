const App = (() => {
  "use strict";

  const chartContainerId = "chart-container";
  const baseUrl = "https://stocksdata-backend-88888.uw.r.appspot.com/api/hourly";
  let ticker = null;
  let chartContainer = null;

  const showHistoricChart = (items) => {
    console.log("inside build chart");
    console.log(items.candledata.c);
    const ohlc = [];
    const volume = [];

     for (var i=0; i < items.candledata.c.length; i += 1) {
        ohlc.push([
            items.candledata.t[i]*1000,
            items.candledata.o[i], // the date
            items.candledata.h[i], // open
            items.candledata.l[i],// high
            items.candledata.c[i], // low
        ]);

        volume.push([
          items.candledata.t[i]*1000, // the date
          items.candledata.v[i], // the volume
        ]);
      }


    console.log("ohlc");
    console.log(ohlc);
    console.log("volume");
    console.log(volume);


    const chart = Highcharts.stockChart(chartContainerId, {
      time: {
        useUTC: false,
      },

      rangeSelector: {
        selected: 2,
      },

      margin: 0,

      yAxis: [
        {
          startOnTick: false,
          endOnTick: false,
          labels: {
            align: "right",
            x: -3,
          },
          title: {
            text: "OHLC",
          },
          height: "60%",
          lineWidth: 2,
          resize: {
            enabled: true,
          },
        },
        {
          labels: {
            align: "right",
            x: -3,
          },
          title: {
            text: "Volume",
          },
          top: "65%",
          height: "35%",
          offset: 0,
          lineWidth: 2,
        },
      ],

      tooltip: {
        split: true,
        xDateFormat: "%A, %b %e, %Y",
      },

      plotOptions: {
        series: {
          dataGrouping: {
            units: [
              ["week", [1]],
              ["month", [1, 2, 3, 4, 6]],
            ],
          },
        },
      },

      series: [
        {
          type: "candlestick",
          name: ticker,
          id: ticker.toLowerCase(),
          zIndex: 2,
          data: ohlc,
          tooltip: {
            valueDecimals: 2,
          },
        },
        {
          type: "column",
          name: "Volume",
          id: "volume",
          data: volume,
          yAxis: 1,
        },
        {
          type: "vbp",
          linkedTo: ticker.toLowerCase(),
          params: {
            volumeSeriesID: "volume",
          },
          dataLabels: {
            enabled: false,
          },
          zoneLines: {
            enabled: false,
          },
          tooltip: {
            valueDecimals: 2,
          },
        },
        {
          type: "sma",
          linkedTo: ticker.toLowerCase(),
          zIndex: 1,
          marker: {
            enabled: false,
          },
          tooltip: {
            valueDecimals: 2,
          },
        },
      ],
    });

  };

  const showChartHourly = (items) => {
        var data = [];
        var hrlydata = [];
        var hrlydataasc = [];

        for (var i=items.candledata.c.length-1; i >=0; i --) {
          data.push([
              items.candledata.t[i]*1000,
              items.candledata.c[i],
          ]);
        }
      var latest = items.candledata.t[items.candledata.c.length-1]*1000;
      console.log("epoch date is "+latest);
      var myDate = new Date(latest);
      console.log("current date is "+myDate);
      var last = new Date(myDate.getTime() - (6 * 60 * 60 * 1000));
      console.log("6 hours back is "+last );
      var epoch6hoursback  = (last.getTime()/1000);
      console.log("6 hours back epoch is "+epoch6hoursback );

      for (var i=items.candledata.c.length-1; i >=0; i --) {
          if(items.candledata.t[i]>= epoch6hoursback) {
            hrlydata.push([
              items.candledata.t[i]*1000,
              items.candledata.c[i],
          ]);
          }
        }

      hrlydata = hrlydata.reverse();


      console.log("finalData");
      console.log(hrlydata);

      var color;
      if(this.changeinprice>0)
        color = '#56ad05';

      else {
        color = 'red'
      }

      var dateFormat = '%H:%M';
      const chart = Highcharts.chart(chartContainerId, {
        rangeSelector: {
            enabled: false
        },

        navigator: {
          enabled: false
        },

        title: {
            text: ticker+ ' Hourly Price Variation',
        },

        series: [{
            name: ticker,
            type: 'line',
            data: hrlydata,
            tooltip: {
                valueDecimals: 2
            },
            color: color,
        }],

        xAxis: {
          dateTimeLabelFormats: {

            minute: dateFormat,
            hour: dateFormat,

        },
          labels: {
              rotation: 0
          },
          title: {
              text: "Stock Data"
          },
          type: "datetime",
      },

      yAxis: {
        opposite: true
      }

    });
    }

  const HistoricChart = () => {
    class CheckedError extends Error {
      constructor(message) {
        super(message);
      }
    }

    fetch(`https://stocksdata-backend-88888.uw.r.appspot.com/api/candles/${ticker}`)
      .then((response) => {

          return response.json();
      })
      .then((responseJson) => {
        console.log(responseJson);

        showHistoricChart(responseJson);
      })
  };

  const hourlyChart = () => {
      class CheckedError extends Error {
        constructor(message) {
          super(message);
        }
      }

      fetch(`https://stocksdata-backend-88888.uw.r.appspot.com/api/hourly/${ticker}`)
        .then((response) => {

            return response.json();
        })
        .then((responseJson) => {
          console.log(responseJson);
          showChartHourly(responseJson);
        })
    };


  const init = () => {
     const params = new URLSearchParams(window.location.search);
    ticker = params.get("ticker");
    chartContainer = document.getElementById(chartContainerId);
    if(ticker.includes("hourly")) {
        ticker = ticker.substring(0,ticker.indexOf("hourly"));
        hourlyChart();

    }
    else {
    HistoricChart();
    }
  };

  return {
    init,
  };
})();