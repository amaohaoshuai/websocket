<template>
  <div class="about">
    <button @click="send">发消息</button>
  </div>
</template>

<script>
export default {
  data () {
    return {
      path: 'ws://127.0.0.1:8082/websocket/websocket/2',
      socket: ''
    }
  },
  mounted () {
    // 初始化
    this.init()
  },
  methods: {
    init: function () {
      if (typeof (WebSocket) === 'undefined') {
        alert('您的浏览器不支持socket')
      } else {
        // 实例化socket
        this.socket = new WebSocket(this.path)
        // 监听socket连接
        this.socket.onopen = this.open
        // 监听socket错误信息
        this.socket.onerror = this.error
        // 监听socket消息
        this.socket.onmessage = this.getMessage
        // 监听socket关闭
        this.socket.onclose = this.close
      }
    },
    open: function () {
      console.log('socket连接成功')
    },
    error: function () {
      console.log('连接错误')
    },
    getMessage: function (msg) {
      console.log(msg.data)
    },
    send: function () {
      const messageModel = {
        fromId: '2',
        toId: '1',
        message: 'from 2 to 1'
      }
      this.socket.send(JSON.stringify(messageModel))
    },
    close: function () {
      console.log('socket已经关闭')
    }
  },
  unmounted () {
    // 销毁监听
    this.socket.onclose = this.close
  }
}
</script>
