const App = (() => {
  "use strict";


  const chartContainerId = "chart-trends";
  const baseUrl = "https://stocksdata-backend-88888.uw.r.appspot.com/api/recommendations";
  let ticker = null;
  let chartContainer = null;

  const buildChart = (items) => {
      var colors = ['#21660a', '#3ecc0e', '#d68d20','#eb2813', '#730f05'];

      var trendsForChart = items.trends
      console.log("inside getCHart");
      console.log(trendsForChart);
      var strongBuyarr = [];
      var buy = [];
      var hold = [];
      var sell = [];
      var strongsSell = [];
      var period = [];

      for(var i=0;i<trendsForChart.length;i++) {
        strongBuyarr[i] = trendsForChart[i].strongBuy;
        period[i] = trendsForChart[i].period;
      }

      for(var i=0;i<trendsForChart.length;i++) {
        buy[i] = trendsForChart[i].buy;
      }

      for(var i=0;i<trendsForChart.length;i++) {
        hold[i] = trendsForChart[i].hold;
      }

      for(var i=0;i<trendsForChart.length;i++) {
        sell[i] = trendsForChart[i].sell;
      }

      for(var i=0;i<trendsForChart.length;i++) {
        sell[i] = trendsForChart[i].sell;
      }

      for(var i=0;i<trendsForChart.length;i++) {
        strongsSell[i] = trendsForChart[i].strongSell;
      }

      const chart = Highcharts.chart(chartContainerId, {
        chart: {
            type: 'column'
        },
        colors: colors,
        title: {
            text: 'Recommendation Trends'
        },
        xAxis: {
            categories: period
        },
        yAxis: {
            min: 0,
            title: {
                text: 'Analysis'
            },
            stackLabels: {
                enabled: true,
                style: {
                    fontWeight: 'bold',

                }
            }
        },
        legend: {
                    borderColor: '#CCC',
                    borderWidth: 1,
                    shadow: false
        },
        tooltip: {
            headerFormat: '<b>{point.x}</b><br/>',
            pointFormat: '{series.name}: {point.y}<br/> Total: 52'
        },
        plotOptions: {
            column: {
                stacking: 'normal',
                dataLabels: {
                    enabled: true
                }
            }
        },
        series: [{
            name: 'Strong Buy',
            data: strongBuyarr,
            type: "column",
        }, {
            name: 'Buy',
            data:  buy,
            type: "column",
        }, {
            name: 'Hold',
            data:  hold,
            type: "column",
        },
        {
          name: 'Sell',
          data:  sell,
          type: "column",
      },
      {
        name: 'Strong Sell',
        data:  strongsSell,
        type: "column",
    },

      ]
    });

    }

  const fetchDataAndLoadChart = () => {
    class CheckedError extends Error {
      constructor(message) {
        super(message);
      }
    }

    fetch(`${baseUrl}/${ticker}`)
      .then((response) => {

          return response.json();
      })
      .then((responseJson) => {
        console.log(responseJson);
        console.log("before showChart");
        console.log(responseJson);
        buildChart(responseJson);
      })
  };

  const init = () => {
    const params = new URLSearchParams(window.location.search);
    ticker = params.get("ticker");
    chartContainer = document.getElementById(chartContainerId);
    fetchDataAndLoadChart();
  };

  return {
    init,
  };
})();