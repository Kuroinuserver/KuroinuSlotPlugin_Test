# KuroinuSLotPlugin_Test
## コマンド
### <>は必須項目、()はオプション項目です
/kslot
- create
  - help
  - <スロット名>
- place
  - <スロット名>
    - <スロットID>
- edit
  - <スロット名>
    - coin
    - reward
    - symbol
      - <シンボルID>
        - handitem
- debug
  - isPlaying
    - true
      - (プレイヤー名)
    - false
      - (プレイヤー名)
## .ymlファイル
### config.yml
### slots/<スロット名>.yml
name:
displayname:
tick:
delay:
coin:
- money:
- item:
slot:
- <アイテムナンバー>
  - reward:
    - <リワードナンバー>
      - item
  - percent:
symbol:
- <シンボルナンバー>
  - item
startsound:
ticksound:
### placedslot/<スロットID>.yml
slot:
itemframe1:
itemframe2:
itemframe3:
lever:
sign:
stock:
