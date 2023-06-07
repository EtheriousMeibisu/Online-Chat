# Online-Chat Курсовой проект "Сетевой чат"

# Описание проекта
Я разработала два приложения для обмена текстовыми сообщениями по сети с помощью консоли (терминала) между двумя и более пользователями.

Первое приложение - сервер чата, должно ожидать подключения пользователей.

Второе приложение - клиент чата, подключается к серверу чата и осуществляет доставку и получение новых сообщений.

Все сообщения записываются в log.txt как на сервере, так и на клиентах. log.txt дополняeтcя при каждом запуске, а также при отправленном или полученном сообщении. Выход из чата осуществлен по команде exit.

# Требования к серверу
Установка порта для подключения клиентов через файл настроек (например, settings.txt);
Возможность подключиться к серверу в любой момент и присоединиться к чату;
Отправка новых сообщений клиентам;
Запись всех отправленных через сервер сообщений с указанием имени пользователя и времени отправки.
# Tребования к клиенту
Выбор имени для участия в чате;
Прочитать настройки приложения из файла настроек - например, номер порта сервера;
Подключение к указанному в настройках серверу;
Для выхода из чата нужно набрать команду выхода - “/exit”;
Каждое сообщение участников должно записываться в текстовый файл - файл логирования. При каждом запуске приложения файл должен дополняться.
