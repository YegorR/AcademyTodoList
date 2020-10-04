# TodoList API


## Сущности:

### Дело:

id - целое,

creationDate - 30-11-2020,

updateDate - 1-12-2020,

name - "Написать курсовую",

description - "Необходимо написать курсовую",

priority - от 1 до 5, где 1 - меньший приоритет, 5 - наибольший,

done - true/false


### Список дел

id - целое,

creationDate - 30-11-2020,

updateDate - 1-12-2020,

name - "Дела по учёбе"



## Методы

### Получить списки

GET /lists

Параметры:

limit - целочисленное неотрицательное; при limit>100 принимает значение по умолчанию. Значение по умолчанию "10".

sort - подчиняется шаблону "X[:asc|desc][,X[:asc|desc][,...]]" (!!!посмотри потом про regex что-ли!!!), где X принимает значение "creation_date", "update_date" или "name"; asc - сортировка по возрастанию, desc - сортировка по убыванию, если не указано, то принимается сортировка по возрастанию. Сначала идёт сортировка по первому полю, потом по второму, потом по третьему. По умолчанию - сортировка по имени по возрастанию.

filter - = (нерегистрозависимый), >, <, >=, <=, &, |, !. Например: "update_date>30-11-2020&name='Такой-то список дел'" (!!!потом конкретизировать!!!)

Ответ:
{
	openedListsCount: 2,
	closedListsCount: 3,
	lists: [
		{
			id: 1,
			name: "Мой хороший список",
			creationDate: 28-09-2020,
			updateDate: 29-09-2020
		},
		...
	]
}

### Добавить список

POST /list

Тело запроса:

{
	name: "Мой хороший список"
}

Ответ: 

{
	id: 1,
	name: "Мой хороший список",
	creationDate: 28-09-2020,
	updateDate: 28-09-2020
}

### Изменить список 

PUT /list/{id}

Тело запроса:

{
	name: "Мой плохой список"
}

Ответ:

{
	id: 1,
	name: "Мой плохой список",
	creationDate: 28-09-2020,
	updateDate: 29-09-2020
}

### Удалить список 

DELETE /list/{id}

Ответ: пустой


### Получить список

GET /list/{id}


Ответ:

{
	id: 1,
	name: "Мой хороший список",
	creationDate: 28-09-2020,
	updateDate: 29-09-2020,
	closedTasksCount: 1,
	openedTasksCount: 2,
	tasks: [
		{
			id: 2,
			listId: 1,
			creationDate: 30-11-2020,
			updateDate: 1-12-2020,
			name: "Написать курсовую",
			description: "Необходимо написать курсовую",
			priority: 2,
			done: true
		},
		...
	]
}

### Добавить дело

POST /task 

Тело запроса:

{
	listId: 2
	name: "Написать курсовую",
	description: "Эх бы вот написать курсовую",
	priority: 3
}

Ответ:

{
	id: 2,
	listId: 1,
	creationDate: 30-11-2020,
	updateDate: 30-11-2020,
	name: "Написать курсовую",
	description: "Необходимо написать курсовую",
	priority: 2,
	done: false
}


### Изменить дело

PUT /task/{id}

Тело запроса:

{
	listId: 2,
	name: "Написать курсовую",
	description: "Необходимо СРОЧНО написать курсовую",
	priority: 5,
	done: true
}

Ответ:

{
	id: 2,
	listId: 2,
	creationDate: 30-11-2020,
	updateDate: 30-11-2020,
	name: "Написать курсовую",
	description: "Необходимо написать курсовую",
	priority: 2,
	done: false
}


### Пометить дело как сделанное

POST /mark-done/{id}

Ответ:
	true


### Удалить дело

DELETE /task/{id}


Ответ: пустой

