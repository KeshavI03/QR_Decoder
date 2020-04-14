# QR_Decoder
## Introduction
This project is a basic Java implementation qr detection used in modern qr applications that looks to achieve basic functionality and usage

## Note
This projects aims to be an intro to qr systems, and so, does not provide the adaptability and versatility of modern systems. As a result, it performs notably worse at detecting qr codes that are large versions, far away, rotated, or at angles. Improving the detection system is one of the possible improvements that can be made to this system. Secondly, the current version is only able to decode up to version 2 QR codes. This may change in the future.


## Sources
- Huge thanks to Utkarsh Sinha from [AI Shack](https://aishack.in/) for his clear description of QR Code detection and finder patterns in his tutorial [Scanning QR Codes](https://aishack.in/tutorials/scanning-qr-codes-1/) 
- Huge thanks to Andrew Fuller for his blog [Decoding small QR codes by hand](https://blog.qartis.com/decoding-small-qr-codes-by-hand/) for help on masking patterns
- Finally, a huge thanks to [Thonky.com](https://www.thonky.com/qr-code-tutorial/) for explaination of text encoding and module placement

## Instalation
Download the zip file of the project and extract the contents </br>
- If you want to run tester.java example file, copy the four classes in the decoder class into the usage folder
- If you are working in Eclipse, right click on your source package, click Import, and select the decoder folder you imported, then recompile

Importing in Java:
```java

import package_name.decoder.*;

```
OR
```java

import package_name.decoder.Pixel;
import package_name.decoder.point;
import package_name.decoder.qrCode;
import package_name.decoder.qrDecoder;

```

## Usage

- Create folder named "images" in the same folder as your main class, in which you place the images you want to extract a QR Code from
- If you call the ```drawImage();``` and/or ```drawQRImage(15);``` methods, then create a folder named "images output" in the same folder as your main class, and within that two folders named "highlights" and "representations". These folders will contain the outputs of the two methods respectively

## License
[MIT](https://choosealicense.com/licenses/mit/)

QR Code is registered trademark of [DENSO WAVE](https://www.denso-wave.com/en/) inc.
