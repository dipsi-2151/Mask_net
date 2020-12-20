function openNav() {
  document.getElementById("mySidenav").style.width = "250px";
}

function closeNav() {
  document.getElementById("mySidenav").style.width = "0";
}

let page = 0;
let resp_type;
function GenerateTable(page, resp_type) {
var resp_type = resp_type;
  var url = "http://52.188.166.61:7000/" + resp_type + "?skip=" + page;
  console.log(url);
  var settings = {
    url: url,
    method: "GET",
    timeout: 0,
  };
  $.ajax(settings).done(function (response) {
    let customers = response;

    //Create a HTML Table element.
    var table = document.createElement("TABLE");
    table.border = "1";

    //Get the count of columns.
    var columnCount = customers.length;

    //Add the header row.
    var row = table.insertRow(-1);
    for (let key in customers[0]) {
      var headerCell = document.createElement("TH");
      headerCell.innerHTML = key;
      row.appendChild(headerCell);
    }

    //Add the data rows.
    for (var i = 0; i < customers.length; i++) {
      row = table.insertRow(-1);
      for (let key in customers[i]) {
        var cell = row.insertCell(-1);
        if (key == "img") {
          var image = new Image();
          cell.innerHTML =
            "<img src=data:image/png;base64," +
            customers[i][key] +
            ' width="300" height="200">';
        } else {
          cell.innerHTML = customers[i][key];
        }
      }
    }

    var dvTable = document.getElementById("dvTable");
    dvTable.innerHTML = "";
    dvTable.appendChild(table);
  });
}
function Next(resp_type) {
  page = page + 1;
  console.log(page);
  GenerateTable(page, resp_type);
}
