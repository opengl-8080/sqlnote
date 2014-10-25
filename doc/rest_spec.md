#REST Spec
##リクエスト一覧
- 全 SQL メモのタイトルと ID を問い合わせる
- SQL メモを新規に登録する
- SQL メモを削除する
- ID を指定して SQL メモの情報を取得する
- SQL メモの情報を更新する
- SQL を実行し、結果を取得する
- データベースの接続先を追加する
- データベースの接続先を削除する
- データベースの接続先を更新する
- データベースに接続できるか検証する
- データベースの接続先一覧を取得する

*****************************************************************************************
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
`/api/sql/{id}/result?s[CODE]=AAA&s[AGE]=20&dataSource=1`

###Status Code
|コード|説明|
|:--|:--|
|200|正常終了|
|303|検索結果が 1,000 件より多い|
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
	- `{0} はパラメータに宣言されていません。`

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

####303 See Other
```json
{
	"recordCount": 1500,
	"url": "http://localhost:1234/sqlnote/api/sql/5/result?s[CODE]=AAA&s[AGE]=20&dataSource=1&type=csv"
}
```

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
