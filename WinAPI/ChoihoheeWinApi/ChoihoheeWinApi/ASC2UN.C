/* Chapter 2. Simple Asc2Un implementation */
#include "EvryThng.h"

#define BUF_SIZE 256

BOOL Asc2Un(LPCTSTR fIn, LPCTSTR fOut, BOOL bFailIfExists)

/* ASCII to Unicode file copy function  - Simple implementation
 *      fIn:      Source file pathname
 *      fOut:      Destination file pathname
 *      bFailIfExists:   Do not copy if the destination file already exists
 *   Behavior is modeled after CopyFile */
{
    HANDLE hIn, hOut;
    DWORD fdwOut, nIn, nOut, iCopy;
    CHAR aBuffer[BUF_SIZE] = { 0, };
    WCHAR uBuffer[BUF_SIZE];
    BOOL WriteOK = TRUE;
    int i;


    hIn = CreateFile(fIn, GENERIC_READ, 0, NULL, OPEN_EXISTING, FILE_ATTRIBUTE_NORMAL, NULL);

    if (hIn == INVALID_HANDLE_VALUE) return FALSE;

    fdwOut = bFailIfExists ? CREATE_NEW : CREATE_ALWAYS;

    hOut = CreateFile(fOut, GENERIC_WRITE, 0, NULL, fdwOut, FILE_ATTRIBUTE_NORMAL, NULL);

    if (hOut == INVALID_HANDLE_VALUE) return FALSE;

    while (ReadFile(hIn, aBuffer, BUF_SIZE, &nIn, NULL) && nIn > 0 && WriteOK) {
        for (iCopy = 0; iCopy < nIn; iCopy++)
            uBuffer[iCopy] = (WCHAR)aBuffer[iCopy];

        WriteOK = WriteFile(hOut, uBuffer, 2 * nIn, &nOut, NULL);
    }
    //uBuffer�� char ������ p�� ����ȯ�� ��, p�� ���� �޸��� �Ϻθ� ������      �� ���� ���
    char* p;
    p = (char*)uBuffer;

    //aBuffer �迭�� ��Ҹ� �ݺ��� ASCII ���ڷ� ���
    for (i = 0; aBuffer[i] != '\0'; i++)
        printf("%c", aBuffer[i]);
    printf("\n");

    //aBuffer�迭�� \\uXXXX �������� 16������ ���
    for (i = 0; aBuffer[i] != '\0'; i++)
        printf("\\u%04x", aBuffer[i]);
    printf("\n");

    //uBuffer�� �޸𸮰��� 16������ ���
    for (i = 0; i < 16; i++)
        printf("%x ", p[i]);
    printf("\n");

    CloseHandle(hIn);
    CloseHandle(hOut);

    return WriteOK;
}