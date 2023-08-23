<?php

/**
 * PDO mysql database helper class
 *
 * @author wildantea <wildannudin@gmail.com>
 * @copyright june 2013
 */
class Database {

    private $pdo;

    public function __construct() {
        try {
            $this->pdo = new PDO(DB_DSN, DB_USERNAME, DB_PASSWORD, array(PDO::MYSQL_ATTR_INIT_COMMAND => "SET NAMES '" . DB_CHARACSET . "';"));
            $this->pdo->exec("SET CHARACTER SET " . DB_CHARACSET);
            $this->pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
            $this->pdo->query("set names " . DB_CHARACSET);
        } catch (PDOException $e) {
            echo "error " . $e->getMessage();
        }
    }

    /**
     * custom query ,update,delete,insert,or fetch, joining multiple table etc, aritmathic etc
     * @param  string $sql  custom query
     * @param  array $data associative array
     * @return array  recordset
     */
    public function custom_query($sql, $data = null) {
        if ($data !== null) {
            $dat = array_values($data);
        }
        $sel = $this->pdo->prepare($sql);
        if ($data !== null) {
            $sel->execute($dat);
        } else {
            $sel->execute();
        }
        $sel->setFetchMode(PDO::FETCH_OBJ);
        return $sel;
    }

    /**
     * Find Count Of rows in datbase
     * @param  string $sql  custom query
     * @return   rowCount
     */
    public function countRow($sql) {

        $sel = $this->pdo->prepare($sql);

        $sel->execute();
        $c = $sel->fetchColumn();
        return $c;
    }

    /**
     * begin a transaction.
     */
    public function begin_transaction() {
        $this->pdo->setAttribute(PDO::ATTR_AUTOCOMMIT, 0);
        $this->pdo->beginTransaction();
    }

    /**
     * commit the transaction.
     */
    public function commit() {
        $this->pdo->commit();
        $this->pdo->setAttribute(PDO::ATTR_AUTOCOMMIT, 1);
    }

    /**
     * rollback the transaction.
     */
    public function rollback() {
        $this->pdo->rollBack();
        $this->pdo->setAttribute(PDO::ATTR_AUTOCOMMIT, 1);
    }

    /**
     * fetch only one row
     * @param  string $table table name
     * @param  string $col condition column
     * @param  string $val value column
     * @return array recordset
     */
    public function fetch_single_row($table, $col, $val) {
        $nilai = array($val);
        $sel = $this->pdo->prepare("SELECT * FROM $table WHERE $col=?");
        $sel->execute($nilai);
        $sel->setFetchMode(PDO::FETCH_OBJ);
        $obj = $sel->fetch();
        return $obj;
    }

    /**
     * fetch all data
     * @param  string $table table name
     * @return array recordset
     */
    public function fetch_all($table) {
        $sel = $this->pdo->prepare("SELECT * FROM $table");
        $sel->execute();
        $sel->setFetchMode(PDO::FETCH_OBJ);
        return $sel;
    }
    
    /**
     * fetch all Order By data
     * @param  string $table table name
     * @return array recordset
     */
    public function fetch_all_order($table,$id) {
        $sel = $this->pdo->prepare("SELECT * FROM $table ORDER BY $id ASC");
        $sel->execute();
        $sel->setFetchMode(PDO::FETCH_OBJ);
        return $sel;
    }

    /**
     * fetch multiple row
     * @param  string $table table name
     * @param  array $dat specific column selection
     * @return array recordset
     */
    public function fetch_col($table, $dat) {
        if ($dat !== null)
            $cols = array_values($dat);
        $col = implode(', ', $cols);
        $sel = $this->pdo->prepare("SELECT $col from $table");
        $sel->execute();
        $sel->setFetchMode(PDO::FETCH_OBJ);
        return $sel;
    }

    /**
     * fetch row with condition
     * @param  string $table table name
     * @param  array $col which columns name would be select
     * @param  array $where what column will be the condition
     * @return array recordset
     */
    public function fetch_multi_row($table, $col, $where) {

        $data = array_values($where);
        //grab keys
        $cols = array_keys($where);
        $colum = implode(', ', $col);
        foreach ($cols as $key) {
            $keys = $key . "=?";
            $mark[] = $keys;
        }

        $jum = count($where);
        if ($jum > 1) {
            $im = implode(' and  ', $mark);
            $sel = $this->pdo->prepare("SELECT $colum from $table WHERE $im");
        } else {
            $im = implode('', $mark);
            $sel = $this->pdo->prepare("SELECT $colum from $table WHERE $im");
        }
        $sel->execute($data);
        $sel->setFetchMode(PDO::FETCH_OBJ);
        return $sel;
    }

    /**
     * fetch row with condition
     * @param  string $table table name
     * @param  array $col which columns name would be select
     * @param  array $where what column will be the condition
     * @return array recordset
     */
    public function fetch_multi_row_array($table, $col, $where) {

        $data = array_values($where);
        //grab keys
        $cols = array_keys($where);
        $colum = implode(', ', $col);
        foreach ($cols as $key) {
            $keys = $key . "=?";
            $mark[] = $keys;
        }

        $jum = count($where);
        if ($jum > 1) {
            $im = implode(' and  ', $mark);
            $sel = $this->pdo->prepare("SELECT $colum from $table WHERE $im");
        } else {
            $im = implode('', $mark);
            $sel = $this->pdo->prepare("SELECT $colum from $table WHERE $im");
        }
        $sel->execute($data);
        return $sel->fetchAll();
    }

    /**
     * fetch row with condition
     * @param  string order ASC or DESC
     * @param  string $index based on which column
     * @param  string $table table name
     * @param  array $col which columns name would be select
     * @param  array $where what column will be the condition
     * @return array recordset
     */
    public function fetch_multi_row_order($table, $col, $where, $order, $index) {

        $data = array_values($where);
        //grab keys
        $cols = array_keys($where);
        $colum = implode(', ', $col);
        foreach ($cols as $key) {
            $keys = $key . "=?";
            $mark[] = $keys;
        }

        $jum = count($where);
        if ($jum > 1) {
            $im = implode('? and  ', $mark);
            $sel = $this->pdo->prepare("SELECT $colum from $table WHERE $im ORDER BY $index $order");
        } else {
            $im = implode('', $mark);
            $sel = $this->pdo->prepare("SELECT $colum from $table WHERE $im ORDER BY $index $order");
        }
        $sel->execute($data);
        $sel->setFetchMode(PDO::FETCH_OBJ);
        return $sel;
    }

    /**
     * check if there is exist data
     * @param  string $table table name
     * @param  array $dat array list of data to find
     * @return true or false
     */
    public function check_exist($table, $dat) {

        $data = array_values($dat);
        //grab keys
        $cols = array_keys($dat);
        $col = implode(', ', $cols);

        foreach ($cols as $key) {
            $keys = $key . "=?";
            $mark[] = $keys;
        }

        $jum = count($dat);
        if ($jum > 1) {
            $im = implode(' and  ', $mark);
            $sel = $this->pdo->prepare("SELECT $col from $table WHERE $im");
        } else {
            $im = implode('', $mark);
            $sel = $this->pdo->prepare("SELECT $col from $table WHERE $im");
        }
        $sel->execute($data);
        $sel->setFetchMode(PDO::FETCH_OBJ);
        $jum = $sel->rowCount();
        if ($jum > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * search data
     * @param  string $table table name
     * @param  array $col   column name
     * @param  array $where where condition
     * @return array recordset
     */
    public function search($table, $col, $where) {
        $data = array_values($where);
        foreach ($data as $key) {
            $val = '%' . $key . '%';
            $value[] = $val;
        }
        //grab keys
        $cols = array_keys($where);
        $colum = implode(', ', $col);

        foreach ($cols as $key) {
            $keys = $key . " LIKE ?";
            $mark[] = $keys;
        }
        $jum = count($where);
        if ($jum > 1) {
            $im = implode(' OR  ', $mark);
            $sel = $this->pdo->prepare("SELECT $colum from $table WHERE $im");
        } else {
            $im = implode('', $mark);
            $sel = $this->pdo->prepare("SELECT $colum from $table WHERE $im");
        }

        $sel->execute($value);
        $sel->setFetchMode(PDO::FETCH_OBJ);
        return $sel;
    }

    /**
     * get last insert id
     * @return int last insert id
     */
    public function get_last_id() {
        return $this->pdo->lastInsertId();
    }

    /**
     * insert data to table
     * @param  string $table table name
     * @param  array $dat   associative array 'column_name'=>'val'
     */
    public function insert($table, $dat) {

        if ($dat !== null)
            $data = array_values($dat);
        //grab keys
        $cols = array_keys($dat);
        $col = implode(', ', $cols);

        //grab values and change it value
        $mark = array();
        foreach ($data as $key) {
            $keys = '?';
            $mark[] = $keys;
        }
        $im = implode(', ', $mark);
        $ins = $this->pdo->prepare("INSERT INTO $table ($col) values ($im)");
        return $ins->execute($data);
    }

    /**
     * update record
     * @param  string $table table name
     * @param  array $dat   associative array 'col'=>'val'
     * @param  string $id    primary key column name
     * @param  int $val   key value
     */
    public function update($table, $dat, $id, $val) {
        if ($dat !== null)
            $data = array_values($dat);
        array_push($data, $val);
        //grab keys
        $cols = array_keys($dat);
        $mark = array();
        foreach ($cols as $col) {
            $mark[] = $col . "=?";
        }
        $im = implode(', ', $mark);
        $ins = $this->pdo->prepare("UPDATE $table SET $im where $id=?");
        $ins->execute($data);
    }

    function updateMultiWhereCondtion($table, $col, $where) {

        $data = array_values($col);
        //grab keys
        $cols = array_keys($col);
        $mark = array();
        foreach ($cols as $col1) {
            $mark[] = $col1 . "=?";
        }
        $colum = implode(',', $mark);
        $whereData = array_values($where);
        //grab keys
        $whereCols = array_keys($where);
        $mark1 = array();
        foreach ($whereCols as $key) {
            $keys = $key . "=?";
            $mark1[] = $keys;
        }

        foreach ($whereData as $d) {
            array_push($data, $d);
        }

        $jum = count($where);
        if ($jum > 1) {
            $im = implode(' and  ', $mark1);
            $sel = $this->pdo->prepare("UPDATE $table SET $colum where $im");
        } else {
            $im = implode('', $mark1);
            $sel = $this->pdo->prepare("UPDATE $table SET $colum where $im");
        }
        return $sel->execute($data);
    }

    /**
     * delete record
     * @param  string $table table name
     * @param  string $where column name for condition (commonly primay key column name)
     * @param   int $id   key value
     */
    public function delete($table, $where, $id) {
        $data = array($id);
        $sel = $this->pdo->prepare("Delete from $table where $where=?");
        return $sel->execute($data);
    }

    public function __destruct() {
        $this->pdo = null;
    }

}

?>
