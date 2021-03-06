#REST Spec
##全 SQL メモのタイトルと ID を問い合わせる
###Method
`GET`

###Path
`/api/sql`

###Status Code
|コード|説明|
|:--|:--|
|200|正常終了|

###Response
####200 OK
```json
[
	{
		"id": 1,
		"title": "～～を検索する",
		"url": "http://localhots:1234/sqlnote/api/sql/1"
	},
	{
		"id": 2,
		"title": "～～を検索する",
		"url": "http://localhots:1234/sqlnote/api/sql/2"
	}
]
```

*****************************************************************************************
##SQL メモを新規に登録する
###Method
`POST`

###Path
`/api/sql`

###Status Code
|コード|説明|
|:--|:--|
|201|作成完了|

*****************************************************************************************
##SQL メモをコピー登録する
###Method
`POST`

###Path
`/api/sql/{id}`

###Status Code
|コード|説明|
|:--|:--|
|201|作成完了|

コピーされた SQL の名前は `<元の名前> - Copy` で登録される

*****************************************************************************************
##SQL メモを削除する
###Method
`DELETE`

###Path
`/api/sql/{id}`

###Status Code
|コード|説明|
|:--|:--|
|204|削除完了|
|404|指定したIDのSQLが存在しない|

*****************************************************************************************
##ID を指定して SQL メモの情報を取得する
###Method
`GET`

###Path
`/api/sql/{id}`

###Status Code
|コード|説明|
|:--|:--|
|200|正常終了|
|404|指定したIDのSQLが存在しない|

###Response
####200 OK
```json
{
	"id": 5,
	"title": "～～を検索する",
	"sql": "SELECT *\n FROM TEST_TABLE",
	"parameters": [
		{
			"name": "CODE",
			"type": "string"
		},
		{
			"name": "NAME",
			"type": "number"
		}
	],
	"executeSqlUrl": "http://localhost:1234/sqlnote/api/sql/5/result"
}
```

*****************************************************************************************
##SQL メモの情報を更新する
###Method
`PUT`

###Path
`/api/sql/{id}`

###Request Body
```json
{
	"title": "～～を検索する",
	"sql": "SELECT *\n FROM TEST_TABLE",
	"parameters": [
		{
			"name": "CODE",
			"type": "string"
		},
		{
			"name": "AGE",
			"type": "number"
		}
	]
}
```

###Status Code
|コード|説明|
|:--|:--|
|200|更新完了|
|400|入力データに誤りがある|
|404|指定したIDのSQLが存在しない|

####400 Pattern
- title が空
	- `タイトルは必ず指定してください。`
- sql が空
	- `SQL は必ず指定してください。`
- パラメータ名が空
	- `パラメータ名は必ず指定してください。`
- parameters に重複が存在する
	- `パラメータ名が重複しています。`
- パラメータ名に `$` `{` `}` のいずれかが含まれる
	- `パラメータ名に $, {, } は使用できません。`
- sql に、 parameterNames で指定したものとは異なる名前のパラメータが使用されている。
	- `{0} はパラメータに定義されていません。`
	- 出力は、最初に見つかった項目だけで良い。
- parameterNames に sql で使用されていないパラメータが存在する。
	- `{0} は SQL で使用されていません。`

*****************************************************************************************
##SQL を実行し、結果を取得する
###Method
`GET`

###Path
`/api/sql/{id}/result`

###Query Parameter
|パラメータ名|型|必須|説明|例|
|:--|:--|:--|:--|:--|
|s|Map|-|パラメータ名ごとのパラメータ値|s[CODE]=AAA&s[AGE]=12|
|dataSource|int|◯|検索対象のデータソースID|dataSource=1|
|type|string|-|出力形式。現在は csv のみ。|type=csv|

###Status Code
|コード|説明|
|:--|:--|
|200|正常終了|
|400|入力データに誤りがある|
|404|指定したIDのSQLまたはデータソースが存在しない|

####400 Pattern
- パラメータの型が不正
	- `{0} は {1} で指定してください。`
	- 数値型に文字列
	- 数値型が空文字
	- 日付型のフォーマット不正（`yyyy-MM-dd HH:mm:ss` スラッシュ繋ぎ、時分秒省略可）
	- 日付型に空文字
- sql に存在しないパラメータが渡ってきた場合
	- `{0} は SQL で使用されていません。`
- sql で使用されているパラメータが渡ってきていない場合
	- `{0} はパラメータで宣言されていません。`
- データソースが指定されていない場合
	- `データソースが指定されていません。`
- 指定したデータソースが存在しない場合
	- `データソースが存在しません。削除されている可能性があります。`

###Response Body
####200 OK
```json
{
	"metaData": [
		{
			"name": "コード",
			"type": "string"
		},
		{
			"name": "名称",
			"type": "string"
		},
		{
			"name": "作成日",
			"type": "date"
		}
	],
	"data": [
		{
			"コード": "AAA",
			"名称": "BBB",
			"作成日": "2012-01-01T12:00+09:00"
		},
		...
	]
}
```

####CSV Format
```csv
CODE<TAB>NAME<TAB>DATE
aaa<TAB>bbb<TAB>2012-01-01T12:01:01+0900
...
```

delimiter = tab

*****************************************************************************************
##データベースの接続先を追加する
###Method
`POST`

###Path
`/api/database`

###Request Body
```json
{
	"name": "テスト環境",
	"driver": "org.hsqldb.jdbcDriver",
	"url": "jdbc:hsqldb:file:testdb/sqlnote;shutdown=true",
	"userName": "username",
	"password": "password"
}
```

###Status Code
|コード|説明|
|:--|:--|
|201|追加完了|
|202|追加は完了したが、データベース接続に失敗している。|
|400|入力データに誤りがある|

####400 Pattern
- name が空の場合
	- `名前は必ず指定してください。`

###ResponseBody
####202
```json
{
	"message": "<SQLException の message>"
}
```

*****************************************************************************************
##データベースの接続先を削除する
###Method
`DELETE`

###Path
`/api/dataSource/{id}`

###Status Code
|コード|説明|
|:--|:--|
|204|削除完了|
|404|指定した ID のデータベース接続が存在しない|

*****************************************************************************************
##データベースの接続先を更新する
###Method
`PUT`

###Path
`/api/dataSource/{id}`

###Request Body
```json
{
	"name": "試験環境",
	"driver": "org.hsqldb.jdbcDriver",
	"url": "jdbc:hsqldb:file:testdb/sqlnote;shutdown=true",
	"userName": "username",
	"password": "password"
}
```

###Status Code
|コード|説明|
|:--|:--|
|200|更新完了|
|202|追加は完了したが、データベース接続に失敗している。|
|400|入力データに誤りがある|

####400 Pattern
- name が空の場合
	- `名前は必ず指定してください。`

###ResponseBody
####202
```json
{
	"message": "<SQLException の message>"
}
```

*****************************************************************************************
##データベースに接続できるか検証する
###Method
`GET`

###Path
`/api/dataSource/{id}/verify`

###Status Code
|コード|説明|
|:--|:--|
|200|正常に接続できている。|
|202|指定したデータベースへの接続検証でエラーが発生した。|

###Response Body
```json
{
	"message": "<SQLException のメッセージ>"
}
```

*****************************************************************************************
##データベースの接続先一覧を取得する
###Method
`GET`

###Path
`/api/dataSource`

###Status Code
|コード|説明|
|:--|:--|
|200|正常終了|

###Response Body
```json
[
	{
		"id": 1,
		"name": "テスト環境",
		"verifyUrl": "http://localhost:1234/sqlnote/api/dataSource/1/verify"
	},
	{
		"id": 2,
		"name": "本番環境",
		"verifyUrl": "http://localhost:1234/sqlnote/api/dataSource/2/verify"
	},
	...
]
```

*****************************************************************************************
##システムの設定を取得する
###Method
`GET`

###Path
`/api/config`

###Status Code
|コード|説明|
|:--|:--|
|200|正常終了|

###Response Body
```json
{
	"maxRowNum": 300
}
```

*****************************************************************************************
##システムの設定を更新する
###Method
`PUT`

###Path
`/api/config`

###Status Code
|コード|説明|
|:--|:--|
|200|正常終了|
|400|入力データに誤りがある|

####400 Pattern
- `maxRowNum`
	- 未入力の場合
		- `最大出力件数は必ず指定してください。`
	- 0 以下の場合
		- `最大出力件数は 1 以上の値を指定してください。`
	- 数値以外を指定した場合
		- `最大出力件数は数値で指定してください。`

###Request Body
```json
{
	"maxRowNum": 250
}
```

*****************************************************************************************
##Common Response Body
###400 Bad Request
```json
{
	"message": "detail error message"
}
```

###5** Internal Server Error
```json
{
	"message": "detail error message"
}
```
