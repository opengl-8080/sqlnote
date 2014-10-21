#REST Spec
##リクエスト一覧
- 全 SQL メモのタイトルと ID を問い合わせる
- SQL メモを新規に登録する
- SQL メモを削除する
- ID を指定して SQL メモの情報を取得する
- SQL メモの情報を更新する
- SQL を実行し、結果を取得する

-----------------------------------------------------------------------------------------------
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

-----------------------------------------------------------------------------------------------
##SQL メモを新規に登録する
###Method
`POST`

###Path
`/api/sql`

###Status Code
|コード|説明|
|:--|:--|
|201|作成完了|

-----------------------------------------------------------------------------------------------
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

-----------------------------------------------------------------------------------------------
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
	"parameterNames": ["CODE", "NAME"],
	"executeSqlUrl": "http://localhost:1234/sqlnote/api/sql/5/result"
}
```

-----------------------------------------------------------------------------------------------
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
	"parameterNames": ["CODE", "NAME"]
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

-----------------------------------------------------------------------------------------------
##SQL を実行し、結果を取得する
###Method
`GET`

###Path
`/api/sql/{id}/result`

###Request Body
```json
{
	"CODE": "AAA",
	"NAME": "BBB"
}
```

- Search Parameters.

###Status Code
|コード|説明|
|:--|:--|
|200|正常終了|
|303|検索結果が 1,000 件より多い|
|404|指定したIDのSQLが存在しない|

###Response Body
####200 OK
```json
[
	{
		"CODE": "AAA",
		"NAME": "BBB",
		"CREATE_DATE": "2012-01-01T12:00+09:00"
	},
	...
]
```

####303 See Other
```json
{
	"recordCount": 1500,
	"url": "http://localhost:1234/sqlnote/api/sql/5/result?type=csv"
}
```

-----------------------------------------------------------------------------------------------
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
