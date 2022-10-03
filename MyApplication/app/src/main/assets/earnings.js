const App = (() => {
  "use strict";

  const chartContainerId = "chart-earnings";
 const baseUrl = "https://stocksdata-backend-88888.uw.r.appspot.com/api/earnings";
  let ticker = null;
  let chartContainer = null;

  const buildChart = (items) => {
      var colors = ['#21660a', '#3ecc0e', '#d68d20','#eb2813', '#730f05'];

      var earningsResponse = items.earnings
      console.log("inside getCHart");


      var period = [];
      var actual = [];
      var estimate = [];
        var surprise = [];
        var dateRes = [];
        var dateplussurprise = [];



        for(var i=0;i<earningsResponse.length;i++) {
          period[i] = earningsResponse[i].period;

          var datesplit = period[i].split("-");
          var date = "";
          date = date.concat(datesplit[0]+"-");
          date = date.concat(datesplit[1]+"-");
          date = date.concat(datesplit[2].substring(0,2));



          dateRes[i] = date;

          actual[i] = earningsResponse[i].actual;
          estimate[i] = earningsResponse[i].estimate;
          surprise[i] = earningsResponse[i].surprise;

        }

        for(var i=0;i<earningsResponse.length;i++) {
          dateplussurprise[i] = dateRes[i]+"<br/> Surprise : "+surprise[i]
        }

    Highcharts.chart('chart-earnings', {
    chart: {
        type: 'spline',
        backgroundColor: 'white'
    },
    title: {
        text: 'Historical EPS Surprises'
    },
    xAxis: {
        categories: dateplussurprise,
        accessibility: {
            description: 'Months of the year'
        },
    },
    yAxis: {
        title: {
            text: 'Quarterly EPS'
        },
        labels: {
            formatter: function () {
                return this.value;
            }
        }
    },
    tooltip: {
        shared: true
    },
    plotOptions: {
        spline: {
            marker: {
                radius: 4,
                lineColor: '#666666',
                lineWidth: 1
            },

        }
    },
    series: [{
        name: 'Actual',
        marker: {
            symbol: 'circle'
        },
        data: actual

    }, {
        name: 'Estimate',
        marker: {
            symbol: 'diamond',
            fillColor: '#2d26ed'
        },
        data: estimate
    }]
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
          console.log('${baseUrl}/${ticker}');
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