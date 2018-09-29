
for (i = 1; i <= 6; i++) {
    var sel = 'h' + i;
    anchors.add(sel); // show anchors on h1,h2..
    anchors.remove('.page-header > ' + sel); // except for those in .page-header
}


