;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Your solution goes in this file ;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Part 1

;; all-length
(defun all-length (l)
    (cond
        ((null l) 0)
        ((atom l) 1)
        (t (+ (all-length (car l)) (all-length (cdr l))))))

;; range
(defun range (l)
    (list (apply 'min l) (apply 'max l)))

;; before
(defun before (arg1 arg2 l)
    (cond 
        ((null l) nil)
        ((equal (car l) arg1) (cons arg1 (help-before (cdr l) arg2)))
        (t (before arg1 arg2 (cdr l)))))

(defun help-before (l arg2)
    (cond
        ((null l) nil)
        ((equal (car l) arg2) (list arg2))
        (t (cons (car l) (help-before (cdr l) arg2)))))

;; split-if
(defun split-if (f l)
    (list (remove-if f l) (remove-if-not f l)))
    
;; group
(defun group (l n)
    (if (null l)
        nil
        (help-group l nil n)))

(defun help-group (l finall n)
    (cond
        ((null l) (list finall)) ;; final group list when there is no more elm in origial list
        ((eql (length finall) n) (cons finall (help-group l nil n))) ;; cons new group of list with old one
        (t (help-group (cdr l) (append finall (list (car l))) n)))) ;; pass the next elm in fun and create final list

;; mostn
(defun mostn (f l) 
    (if (null l) 
        nil
        (help-mostn f l nil 0)))

;; similar to help-group    
(defun help-mostn (f l finall n)
    (cond
        ((null l) (cons finall (list n)))
        ((> (funcall f (car l)) n) (help-mostn f (cdr l) (list (car l)) (funcall f (car l))))
        ((eql (funcall f (car l)) n) (help-mostn f (cdr l) (append finall (list(car l))) (funcall f (car l))))
        (t (help-mostn f (cdr l) finall n))))
            
;; Part 2

(defun match (p a) 
    (if (> (length p) (length a)) ;;p length cannot be longer then a length
        nil
        (cond
            ((or (null p) (null a)) (eql p a)) ;; when p or a is null compare p and a, for last item return.
            ((eql (car p) '?) (match (cdr p) (cdr a))) ;; "?" condition. igonre now pattern, match cdr list
            ((and (eql (car p) '!) (match (cdr p) (cdr a))) t) ;; "!" condition, main condition is when"!" match and match later are both correct  
            ((eql (car p) '!) (match p (cdr a))) ;; if ! is match more pattern
            (t (and (eql (car p) (car a)) (match (cdr p) (cdr a))))))) ;; continue match cdr, recuresive part. return nil when first elms are not equal, or one of the list empty first.
        
;(print (match '(! x x) '(no matter x x)))

;; Part 3

(defun match-var(p a)
    (if (> (length p) (length a)) ;;same as match
        nil
	    (help-match-var p a nil)))

;; simialr to the match one. just add set
(defun help-match-var(p a finall)
	(cond
		((or (null p) (null a)) (eql p a)) ;; same as match
		
		((and (listp (car p)) (eql (caar p) '?))
		    (set (cadar p) (car a)) (help-match-var (cdr p) (cdr a) nil)) 
			
		((and (listp (car p)) (eql (caar p) '!) (help-match-var (cdr p) (cdr a) finall)) 
		    (set (cadar p) (append finall (list (car a)))) 
		        t)

		((and (listp (car p)) (eql (caar p) '!)) 
		    (help-match-var p (cdr a) (append finall (list (car a))))) 
		
		(t (and (eql (car p) (car a)) (help-match-var (cdr p) (cdr a) nil))))) ;; recursive or exit

;(print (match-var '((! u) c (! w)) '(b c a d)))
;(print (eval 'u))
;(print (eval 'w))
;;(print (range '(5 6 10)) )
;;(print (list (apply 'max '(5 6 10))))
;;(print (group '(5 6 10 1 33 55 66) 2))
