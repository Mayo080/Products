var script = document.createElement('script');
script.src = 'https://html2canvas.hertzen.com/dist/html2canvas.js';
script.onload = function() {

    document.getElementById('captureButton').addEventListener('click', function() {
        captureAndPrint();
    });  

    function captureAndPrint() {
        const screenshotElement = document.getElementById('screenshotArea');

        html2canvas(screenshotElement).then(function(canvas) {
            const croppedCanvas = document.createElement('canvas');
            const context = croppedCanvas.getContext('2d');
            croppedCanvas.width = canvas.width;
            croppedCanvas.height = canvas.height - 35;

            context.drawImage(canvas, 0, 0, canvas.width, canvas.height - 35, 0, 0, canvas.width, canvas.height - 35);

            const printWindow = window.open('', '' );
            printWindow.document.open();
            printWindow.document.write('<img src="' + croppedCanvas.toDataURL() + '" />');
            printWindow.document.close();
        });
    }
};
document.head.appendChild(script);