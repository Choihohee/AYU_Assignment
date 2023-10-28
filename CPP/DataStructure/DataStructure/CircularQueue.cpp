#include <stdio.h>
#include <stdlib.h>
#define QUEUE_SIZE 10

int* queue;
int	front=0;			// 전단
int	rear=0;				// 후단
int size = 1;


// 큐가 공백 상태인지 확인하는 연산
int isEmpty() {
	if (front == rear) 
		return 1;
	else 
		return 0;
}

// 큐가 포화 상태인지 확인하는 연산
int isFull() {
	if (front == (rear + 1) % QUEUE_SIZE) 
		return 1;
	else 
		return 0;
}

int queueSize() {
		return(rear - front + QUEUE_SIZE) % QUEUE_SIZE;
}

// 스택의 top에 원소를 삽입하는 연산
void enqueue(int item) {
	int* temp;

	if (isFull()) {
		printf(" Full QUEUE\n");
	}
	else {
		rear = (rear + 1) % QUEUE_SIZE;
		queue[rear] = item;
	}
}
//스택의 top에 원소를 삭제하는 연산
int dequeue() {
	if (isEmpty()) {	// 큐가 공백 상태인 경우
		printf("\n\n Queue is Empty!!\n");
		return 0;
	}
	else{
		front = (front + 1) % QUEUE_SIZE;
		return queue[front];
	}
	return 0;
}

// 큐의 원소를 출력하는 연산
void printQueue() {
	int i, maxi = rear;

	if (front >= rear) 
		maxi += QUEUE_SIZE;
	printf("Queue size is [%2d]= ", queueSize());

	for (i = front + 1; i <= maxi; i++)
		printf("%2d ", queue[i % QUEUE_SIZE]);
	printf("\n");
}

void main(void) {
	int i;
	queue = (int*)malloc(QUEUE_SIZE * sizeof(int));

	for (i = 0; i < 9; i++) 
		enqueue(i + 1);
	printQueue();
	for (i = 0; i < 7; i++) 
		dequeue();
	printQueue();
	for (i = 10; i < 19; i++) 
		enqueue(i + 1);
	printQueue();

	free(queue);
	getchar(); 
}