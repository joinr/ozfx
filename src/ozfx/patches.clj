(ns ozfx.patches
  (:require [oz.core]
            [oz.server]))

;;monkey-patching oz.server/start-web-server to allow
;;caller to choose NOT to open browser...
(in-ns 'oz.server)
(defn start-web-server! [& [port browse?]]
  (stop-web-server!)
  (let [port (or port 0) ; 0 => Choose any available port
        ring-handler (var main-ring-handler)
        [port stop-fn]
        (let [server (aleph/start-server ring-handler {:port port})
              p (promise)]
          (future @p) ; Workaround for Ref. https://goo.gl/kLvced
          ;; (aleph.netty/wait-for-close server)
          [(aleph.netty/port server)
           (fn [] (.close ^java.io.Closeable server) (deliver p nil))])
        uri (format "http://localhost:%s/" port)]
    (infof "Web server is running at `%s`" uri)
    (when  browse?
      (try
        (.browse (java.awt.Desktop/getDesktop) (java.net.URI. uri))
        (catch java.awt.HeadlessException _)))
    (reset! web-server_ stop-fn)))
(in-ns 'ozfx.demo)

(in-ns 'oz.core)
(defn start-web-server! [& [port browse?]]
  (stop-web-server!)
  (let [port (or port 0) ; 0 => Choose any available port
        ring-handler (var main-ring-handler)
        [port stop-fn]
        (let [server (aleph/start-server ring-handler {:port port})
              p (promise)]
          (future @p) ; Workaround for Ref. https://goo.gl/kLvced
          ;; (aleph.netty/wait-for-close server)
          [(aleph.netty/port server)
           (fn [] (.close ^java.io.Closeable server) (deliver p nil))])
        uri (format "http://localhost:%s/" port)]
    (infof "Web server is running at `%s`" uri)
    (try
      (.browse (java.awt.Desktop/getDesktop) (java.net.URI. uri))
      (catch java.awt.HeadlessException _))
    (reset! web-server_ stop-fn)))
(in-ns 'ozfx.demo)
